import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 从请求体中解构参数
  const { post_id, user_id } = ctx.body;

  if (!post_id || !user_id) {
    return { code: 400, message: '缺少必要参数: post_id 或 user_id' };
  }

  try {
    const db = cloud.database();
    const thumbs = db.collection('posts_thumb');
    const posts = db.collection('posts'); // 获取 posts 表的引用
    const existing = await thumbs.where({ post_id, user_id }).getOne();

    if (existing.data) {
      // 已存在，执行取消点赞
      await thumbs.doc(existing.data._id).remove();
      // 更新 posts 表中对应的记录，将 is_thumb 修改为 false
      await posts.where({ _id: post_id, user_id }).update({ is_thumb: false });
      return { code: 200, message: '取消点赞成功' };
    } else {
      // 不存在，执行点赞
      await thumbs.add({ post_id, user_id });
      // 更新 posts 表中对应的记录，将 is_thumb 修改为 true
      await posts.where({ _id: post_id, user_id }).update({ is_thumb: true });
      return { code: 200, message: '点赞成功' };
    }
  } catch (error) {
    console.error('点赞操作失败:', error);
    return { code: 500, message: '服务器错误，请稍后重试' };
  }
}