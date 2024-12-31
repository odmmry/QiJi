import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 解析查询参数中的帖子 _id
  const { post_id } = ctx.query;

  // 验证查询参数是否包含帖子 _id
  if (!post_id) {
    return {
      code: 400,
      message: '缺少必要参数: post_id'
    };
  }

  try {
    const db = cloud.database();

    // 使用聚合查询，将 posts_comment 表与 users 表进行连接
    const aggregate = db.collection('posts_comment').aggregate()
      .match({ post_id }) // 使用 post_id 作为查询条件
      .lookup({
        from: 'users', // 关联的集合名称
        localField: 'user_id', // 本集合的关联字段
        foreignField: '_id', // 外部集合的关联字段
        as: 'user_info' // 输出的用户信息字段名称
      })
      .project({
        post_id: 1,
        content: 1,
        user_info: { $first: '$user_info' }, // 仅取第一个用户信息
        create_time: 1
      });

    const result = await aggregate.end();

  

    // 返回查询结果，并确保返回的数据结构符合要求
    return {
      code: 200,
      message: '查询成功',
      data: result.data.map(comment => ({
        post_id: comment.post_id,
        content: comment.content,
        nickname: comment.user_info ? comment.user_info.nickname : null,
        avatar_url: comment.user_info ? comment.user_info.avatar_url : null,
        create_time: comment.create_time,
      }))
    };
  } catch (error) {
    console.error('查询失败:', error);
    return {
      code: 500,
      message: '服务器错误，请稍后重试'
    };
  }
}