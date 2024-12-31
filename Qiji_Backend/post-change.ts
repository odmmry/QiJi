import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  const { user_id, nickname, avatar_url } = ctx.body;

  if (!user_id || !nickname || !avatar_url) {
    return { code: 400, message: '缺少必要参数: user_id、nickname 或 avatar_url' };
  }

  try {
    const db = cloud.database();

    // 更新用户信息
    const updateResult = await db.collection('users')
      .where({ _id: user_id }) // 假定 user_id 是用户表中记录的唯一标识符
      .update({
        nickname,
        avatar_url,
      });

    if (updateResult.ok) {
      return { code: 200, message: '用户信息更新成功' };
    } else {
      return { code: 500, message: '更新用户信息失败' };
    }

  } catch (error) {
    console.error('更新用户信息失败:', error);
    return { code: 500, message: '服务器错误，请稍后重试' };
  }
}