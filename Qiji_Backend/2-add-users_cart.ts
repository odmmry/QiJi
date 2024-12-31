import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  const { shop_goods_id, user_id } = ctx.body;

  if (!shop_goods_id || !user_id) {
    return { code: 400, message: '缺少必要参数: shop_goods_id 或 user_id' };
  }

  try {
    const db = cloud.database();

    // 检查购物车中是否已经存在该商品
    const existingItem = await db.collection('users_cart')
      .where({ shop_goods_id, user_id })
      .getOne();

    if (existingItem.data) {
      return { code: 409, message: '该商品已在购物车中' }; // 409 表示冲突
    }

    // 推入购物车
    const result = await db.collection('users_cart').add({
      shop_goods_id,
      user_id,
    });

    return { code: 200, message: '加入购物车成功', data: result.id };
  } catch (error) {
    console.error('加入购物车失败:', error);
    return { code: 500, message: '服务器错误，请稍后重试' };
  }
}