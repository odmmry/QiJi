import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 解析请求体中的参数
  const { user_id, shop_goods_id, address_name, address_phone, address_detail } = ctx.body;

  // 验证必要参数
  if (!user_id || !shop_goods_id) {
    return {
      code: 400,
      message: '缺少必要参数'
    };
  }

  try {
    const db = cloud.database();

    // 查询 shop_goods 表，获取对应的商品记录
    const { data: shopGoods } = await db.collection('shop_goods')
      .where({
        _id: shop_goods_id
      })
      .get();

    // 如果没有找到对应的商品，返回错误
    if (shopGoods.length === 0) {
      return {
        code: 404,
        message: '未找到对应商品'
      };
    }

    // 获取商品信息
    const { price, imgList, name } = shopGoods[0];

    // 插入订单记录到 users_orders 表
    const { id: orderId, ok } = await db.collection('users_orders').add({
      user_id,
      address_name,
      address_phone,
      address_detail,
      total_price: price, // 这里的总价格基于商品价格
      create_time: new Date() // 添加当前时间作为创建时间
    });

    if (!ok) {
      return {
        code: 500,
        message: '订单记录插入失败'
      };
    }

    // 将订单信息插入到 users_orders_goods 表
    const goodsInsertResponse = await db.collection('users_orders_goods').add({
      order_id: orderId, // 关联的订单 ID
      shop_goods_id, // 关联的商品 ID
      imgList, // 商品的图片 URL
      address_detail,
      name, // 商品名称
      price // 商品价格
    });

    if (!goodsInsertResponse.ok) {
      return {
        code: 500,
        message: '订单商品记录插入失败'
      };
    }

    // 返回成功信息
    return {
      code: 200,
      message: '下单成功',
      order_id: orderId, // 返回订单 ID
      total_price: price, // 返回总价格
    };
  } catch (error) {
    console.error('查询失败:', error);
    return {
      code: 500,
      message: '服务器错误，请稍后重试'
    };
  }
}