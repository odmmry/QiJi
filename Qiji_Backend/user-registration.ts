import cloud from '@lafjs/cloud';
import crypto from 'crypto'; // 引入加密模块

export default async function (ctx: FunctionContext) {
  // 解析请求体中的用户数据
  const { username, password, avatar_url, nickname } = ctx.body;

  // 验证请求体是否包含所有必填参数
  if (!username || !password || !nickname) {
    return {
      code: 400,
      message: '缺少必要参数: username, password 或 nickname'
    };
  }

  try {
    // 获取数据库实例
    const db = cloud.database();

    // 检查用户名是否已经存在
    const existingUser = await db.collection('users').where({ username }).getOne();
    if (existingUser.data) {
      return {
        code: 409, // HTTP 状态码 409 表示冲突（Conflict）
        message: '用户名已存在，请选择其他用户名'
      };
    }

    // 使用 MD5 对密码进行加密（注意：生产环境中应使用更安全的哈希算法）
    const hashedPassword = crypto.createHash('md5').update(password).digest('hex');

    // 插入新用户记录到 users 集合
    const result = await db.collection('users').add({
      username,
      nickname, // 添加昵称
      password: hashedPassword, // 存储 MD5 加密后的密码
      avatar_url: avatar_url || '', // 如果没有提供头像链接，则设置为空字符串
      create_time: Date.now() // 使用当前时间戳作为创建时间
    });

    // 返回成功信息以及新用户的 ID
    return {
      code: 200,
      message: '用户注册成功',
      data: { userId: result.id }
    };

  } catch (error) {
    // 捕获错误并返回错误信息
    console.error('用户注册失败:', error);
    return {
      code: 500,
      message: '服务器错误，请稍后重试'
    };
  }
}