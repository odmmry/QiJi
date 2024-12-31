import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 从请求体中解构参数
  const { post_id, content, user_id } = ctx.body;

  // 验证必要参数是否存在
  if (!post_id || !content || !user_id) {
    return { code: 400, message: '缺少必要参数: post_id、content 或 user_id' };
  }

  try {
    const db = cloud.database();
    // 插入评论到 posts_comment 表
    const result = await db.collection('posts_comment').add({
      post_id,
      content,
      user_id,
      create_time: Math.floor(Date.now() / 1000), // 11位时间戳
    });

    return { code: 200, message: '评论添加成功', data: result.id };
  } catch (error) {
    console.error('添加评论失败:', error);
    return { code: 500, message: '服务器错误，请稍后重试' };
  }
}
