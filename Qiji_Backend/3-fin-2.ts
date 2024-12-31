import cloud from '@lafjs/cloud';

export default async function (ctx) {
  const db = cloud.mongo.db;

  // 获取请求中的商品 ID 和用户 ID
  const { _id, user_id } = ctx.query;

  if (!_id || !user_id) {
    return {
      code: 400,
      message: '商品ID和用户ID是必需的',
    };
  }

  try {
    // 查询商品信息
    const goods = await db.collection('shop_goods').findOne({
      _id: _id,
      user_id: user_id,
    });

    if (!goods) {
      return {
        code: 404,
        message: '未找到指定的商品',
      };
    }

    // 跳转到结算界面 test1
    return {
      code: 200,
      message: '跳转到结算界面成功',
      data: {
        redirectUrl: '/test1',
        goods: {
          name: goods.name,
          price: goods.price,
          desc: goods.desc,
          create_time: goods.create_time,
        },
      },
    };
  } catch (error) {
    console.error('查询商品时发生错误:', error);
    return {
      code: 500,
      message: '服务器内部错误',
    };
  }
}
