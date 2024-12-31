import cloud from '@lafjs/cloud';

export default async function (ctx) {
  const db = cloud.mongo.db;

  const { user_id } = ctx.query; // 从请求中获取user_id
  if (!user_id) {
    return {
      code: 404,
      message: 'Missing user_id parameter',
    };
  }

  try {
    // 查询 users_cart 表中与 user_id 相同的记录
    const usersCartCollection = db.collection('users_cart');
    const userCartItems = await usersCartCollection.find({ user_id }).toArray();

    // 获取与 users_cart 表中记录对应的 shop_goods_id
    const shopGoodsIds = userCartItems.map(item => item.shop_goods_id);

    // 查询 shop_goods 表中与 shop_goods_id 相同的记录
    const shopGoodsCollection = db.collection('shop_goods');
    const shopGoodsItems = await shopGoodsCollection.find({ _id: { $in: shopGoodsIds } }).toArray();

    // 将 users_cart 和 shop_goods 数据合并
    const result = userCartItems.map(cartItem => {
      const correspondingGoods = shopGoodsItems.find(good => good._id.toString() === cartItem.shop_goods_id.toString());
      return {
        ...cartItem,
        price: correspondingGoods ? correspondingGoods.price : null,
        imgList: correspondingGoods ? correspondingGoods.imgList : "",
        name: correspondingGoods ? correspondingGoods.name : null,
      };
    });

    // 返回结果，不再使用 items 包裹数据
    return {
      code: 200,
      message: 'Success',
      data: result, // 直接返回合并后的数据
    };
  } catch (err) {
    // 错误处理
    console.error('Error fetching users_cart and shop_goods:', err);
    return {
      code: 500,
      message: 'Internal Server Error',
      error: err.message,
    };
  }
}