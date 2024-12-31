import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 解析查询参数中的帖子 _id 和是否点赞 is_thumb
  const { _id } = ctx.query;

  // 验证查询参数是否包含帖子 _id
  if (!_id) {
    return {
      code: 400,
      message: '缺少必要参数: _id'
    };
  }

  try {
    const db = cloud.database();

    // 使用帖子 _id 查询单个帖子的详细信息
    const postResult = await db.collection('posts').doc(_id).get();

    // 检查是否有匹配的帖子
    if (!postResult.data) {
      return {
        code: 404,
        message: '未找到对应的帖子'
      };
    }

    // 获取帖子用户的 user_id
    const { user_id } = postResult.data;

    // 根据 user_id 查询用户信息
    const userResult = await db.collection('users').where({ _id: user_id }).getOne();

    // 检查是否找到对应的用户
    const userInfo = userResult.data ? {
      nickname: userResult.data.nickname,
      avatar_url: userResult.data.avatar_url
    } : null;

   

    // 返回查询结果，并确保返回的数据结构符合要求
    return {
      code: 200,
      message: '查询成功',
      data: {
        _id: postResult.data._id,
        content: postResult.data.content,
        imgList: postResult.data.imgList || [], // 可能不存在则返回空数组
        nickname: userInfo ? userInfo.nickname : null,
        avatar_url: userInfo ? userInfo.avatar_url : null,
        create_time: postResult.data.create_time,
        is_thumb: postResult.data.is_thumb // 添加是否点赞的参数
      }
    };
  } catch (error) {
    console.error('查询失败:', error);
    return {
      code: 500,
      message: '服务器错误，请稍后重试'
    };
  }
}