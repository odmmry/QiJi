import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 解析查询参数
  const { post_id, user_id, nickname, avatar_url, collectionType } = ctx.query; // 包括集合类型

  try {
    // 获取数据库实例
    const db = cloud.database();
    let result;

    // 根据 collectionType 参数决定查询哪个集合
    if (collectionType === 'users') {
      // 查询 users 集合
      const queryConditions: any = {};
      if (user_id) queryConditions._id = user_id; // 根据 user_id 查询
      if (nickname) queryConditions.nickname = nickname;
      if (avatar_url) queryConditions.avatar_url = avatar_url; // 修改为 avatar_url

      result = await db.collection('users') // 指定集合为 users
        .where(queryConditions) // 按条件筛选
        .get(); // 执行查询并返回数据
    } else {
      // 默认查询 posts 集合并连接用户信息
      const aggregateQuery = db.collection('posts')
        .aggregate()
        .lookup({
          from: 'users', // 需要连接的集合
          localField: 'user_id', // posts 集合中的字段
          foreignField: '_id', // users 集合中的字段
          as: 'user_info' // 输出到 posts 中的字段名
        })
        .project({
          _id: 1, // posts 中的 _id
          content: 1, // posts 中的 content
          user_id: 1, // posts 中的 user_id
          create_time: 1, // posts 中的 create_time
          imgList: 1, // posts 中的 imgList
          nickname: { $arrayElemAt: ['$user_info.nickname', 0] }, // 获取用户的 nickname
          avatar_url: { $arrayElemAt: ['$user_info.avatar_url', 0] } // 修改为获取用户的 avatar_url
        });

      // 根据条件筛选
      if (post_id) aggregateQuery.match({ _id: post_id }); // 根据 post_id 查询
      if (user_id) aggregateQuery.match({ user_id: user_id });
      if (nickname) aggregateQuery.match({ 'user_info.nickname': nickname });
      if (avatar_url) aggregateQuery.match({ 'user_info.avatar_url': avatar_url }); // 修改为对应的 avatar_url

      result = await aggregateQuery.end(); // 执行聚合查询并返回数据
    }

    // 返回查询结果，包括数据和成功信息
    return {
      code: 200, // HTTP 状态码 200 表示请求成功
      message: '查询成功', // 返回消息
      data: result.data.map(item => ({
        _id: item._id,
        content: item.content || null, // posts 特有字段
        user_id: item.user_id,
        create_time: item.create_time || null, // posts 特有字段
        imgList: item.imgList || [], // 包括 imgList（如果存在）
        nickname: item.nickname || null,
        avatar_url: item.avatar_url || ""// 修改为 avatar_url
      })) // 查询到的记录数据，格式化输出
    };
  } catch (error) {
    // 捕获查询过程中的错误，并记录到日志中
    console.error('查询失败:', error);
    // 返回服务器错误信息
    return {
      code: 500, // HTTP 状态码 500 表示服务器内部错误
      message: '服务器错误，请稍后重试' // 提示客户端稍后再试
    };
  }
}