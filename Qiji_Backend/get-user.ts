import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  const { user_id } = ctx.query; // 从请求参数获取 user_id

  if (!user_id) {
    return { code: 400, message: '缺少必要参数: user_id' };
  }

  try {
    const db = cloud.database();

    // 查找用户信息
    const userResult = await db.collection('users')
      .where({ _id: user_id }) // 假定 user_id 是用户表中记录的唯一标识符
      .getOne(); // 获取单条记录

    if (userResult.ok && userResult.data) {
      const { nickname, avatar_url } = userResult.data;
      return { code: 200, 
      data:{nickname, avatar_url} }; // 返回用户信息
    } else {
      return { code: 404, message: '用户未找到' }; // 用户未找到
    }

  } catch (error) {
    console.error('查询用户信息失败:', error);
    return { code: 500, message: '服务器错误，请稍后重试' };
  }
}