import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 解析查询参数中的 user_id
  const { user_id, address_name, address_phone, address_detail } = ctx.body;

  // 验证查询参数是否包含 user_id
  if (!user_id) {
    return {
      code: 400,
      message: '缺少必要参数: user_id'
    };
  }

  try {
    const db = cloud.database();

    // 查询 users_cart 表中与 user_id 匹配的所有记录
    const { data: userCarts } = await db.collection('users_cart')
      .where({ user_id: user_id })
      .get();

    if (!userCarts || userCarts.length === 0) {
      return {
        code: 404,
        message: '未找到对应的购物车记录'
      };
    }

    // 获取所有的 shop_goods_id
    const shopGoodsIds = userCarts.map(item => item.shop_goods_id);

    // 查询 shop_goods 表，获取所有对应的商品记录
    const { data: shopGoods } = await db.collection('shop_goods')
      .where({
        _id: db.command.in(shopGoodsIds) // 使用 in 查询多个 shop_goods_id
      })
      .get();

    // 计算总价格
    const total_price = shopGoods.reduce((total, item) => total + item.price, 0);

    // 插入订单记录到 users_orders 表
    const { id: orderId, ok } = await db.collection('users_orders').add({
      user_id,
      address_name,
      address_phone,
      address_detail,
      total_price,
      create_time: new Date() // 添加当前时间作为创建时间
    });

    if (!ok) {
      return {
        code: 500,
        message: '订单记录插入失败'
      };
    }

    // 将每个商品的相关信息插入到 users_orders_goods 表中
    const ordersGoodsPromises = shopGoods.map(async (item) => {
      return await db.collection('users_orders_goods').add({
        order_id: orderId,
        shop_goods_id: item._id,
        name: item.name,
        price: item.price,
        address_detail: address_detail,
        imgList: item.imgList, // 假设 imgList 是存储商品图片的字段
        create_time: new Date() // 添加当前时间作为创建时间
      });
    });

    // 等待所有商品的信息插入完成
    await Promise.all(ordersGoodsPromises);

    // 删除购物车中对应的记录
    await db.collection('users_cart')
      .where({ user_id: user_id }) // 删除当前用户的所有购物车记录
      .remove({ multi: true }); // 使用 multi: true 确保一次性删除所有记录

    // 返回查询结果，并确保返回的数据结构符合要求
    return {
      code: 200,
      message: '下单成功',
      order_id: orderId, // 返回订单 ID
      total_price: total_price,
      data: userCarts.map(item => ({
        user_id: item.user_id,
        shop_goods_id: item.shop_goods_id,
        create_time: item.create_time,
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