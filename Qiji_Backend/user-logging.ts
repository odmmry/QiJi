import cloud from '@lafjs/cloud';
import { createHash } from 'crypto';

// 云函数导出
export default async function (ctx) {
  // 解析请求体中的用户名和密码
  const { username, password } = ctx.body;

  // 验证请求体是否包含必填参数
  if (!username || !password) {
    return {
      code: 400,
      message: '缺少必要参数: username 或 password'
    };
  }

  try {
    // 获取数据库实例
    const db = cloud.database();

    // 查询用户名对应的用户记录
    const userRes = await db.collection('users').where({ username }).getOne();
    const user = userRes.data; // 从响应中获取用户数据

    // 如果用户不存在，返回未授权错误
    if (!user) {
      return {
        code: 401, // HTTP 状态码 401 表示未授权
        message: '用户名或密码错误'
      };
    }

    // 使用 MD5 对传入的密码进行哈希处理
    const hashedPassword = createHash('md5')
      .update(password)
      .digest('hex');

    // 比较传入的密码与数据库中存储的 MD5 哈希密码
    if (hashedPassword !== user.password) {
      return {
        code: 401, // HTTP 状态码 401 表示未授权
        message: '用户名或密码错误'
      };
    }

    // 如果认证成功，生成一个token并返回给客户端
    const payload = {
      user_id: user._id,
      exp: Math.floor(Date.now() / 1000) + 60 * 60 * 24 * 7 // 有效期为 7 天
    };

    const token = cloud.getToken(payload);

    // 返回成功信息以及token
    return {
      code: 200,
      message: '登录成功',
      data: {user_id:user._id}
    };

  } catch (error) {
    // 捕获错误并返回错误信息
    console.error('登录失败:', error);
    return {
      code: 500, // HTTP 状态码 500 表示服务器内部错误
      message: '服务器错误，请稍后重试'
    };
  }
}