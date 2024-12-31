import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  const db = cloud.mongo.db;

  try {
    // 从请求上下文中获取传入的 order_id
    const { order_id } = ctx.query;

    if (!order_id) {
      return {
        code: 400,
        message: 'order_id is required',
      };
    }

    // 查询 users_orders_goods 集合，获取与指定 order_id 匹配的订单信息
    const order = await db.collection('users_orders_goods').findOne({ order_id });

    if (!order) {
      return {
        code: 404,
        message: 'Order not found',
      };
    }

    // 返回订单信息用于结算页面展示
    return {
      code: 200,
      message: 'Success',
      data: {
        order_id: order.order_id,
        image_url: order.image_url,
        name: order.name,
        price: order.price,
        create_time: order.create_time,
      },
    };
  } catch (error) {
    // 捕获并返回错误信息
    return {
      code: 500,
      message: 'Internal Server Error',
      error: error.message,
    };
  }
}
