import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  try {
    const db = cloud.database();

    // 获取商品列表
    const goods = await db.collection('shop_goods')
      .orderBy('create_time', 'desc')
      .get();

    // 提取所有的 user_id
    const userIds = goods.data.map(item => item.user_id);

    // 筛选出用户记录
    const users = await db.collection('users')
      .where({
        _id: db.command.in(userIds) // 根据 user_id 筛选
      })
      .field({
        nickname: 1,
        avatar_url: 1
      })
      .get();

    // 将用户信息与商品数据结合
    const goodsWithUserInfo = goods.data.map(good => {
      const user = users.data.find(user => user._id === good.user_id);
      return {
        ...good,
        nickname: user ? user.nickname : null,
        avatar_url: user ? user.avatar_url : "",
      };
    });

    return { code: 200, message: '查询成功', data: goodsWithUserInfo };
  } catch (error) {
    console.error('查询商品列表失败:', error);
    return { code: 500, message: '服务器错误，请稍后重试' };
  }
}