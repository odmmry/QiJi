import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  const db = cloud.database();

  // 获取查询参数，例如 user_id
  const { user_id } = ctx.query;

  if (!user_id) {
    return {
      code: 400,
      message: "user_id is required",
    };
  }

  try {
    // 查询条件：根据 user_id 筛选用户订单
    const query = { user_id };

    // 使用聚合查询获取用户订单及对应的商品信息
    const orders = await db.collection('users_orders')
      .aggregate()
      .match(query)
      .lookup({
        from: 'users_orders_goods', // 关联的表
        localField: '_id', // users_orders 表的 _id 字段
        foreignField: 'order_id', // users_orders_goods 表的 order_id 字段
        as: 'goods', // 新字段，存放关联结果
      })
      .project({
        user_id: 1,
        address_name: 1,
        address_phone: 1,
        total_price: 1,
        address_detail: 1,
        goods: 1, // 保留关联商品信息
      })
      .end();

    // 处理结果数据，将每个订单的商品单独映射到各自的订单中
    const result = orders.data.map(order => ({
      _id: order._id,
      user_id: order.user_id,
      address_name: order.address_name,
      address_phone: order.address_phone,
      address_detail: order.address_detail,
      total_price: order.total_price,
      order_goods: order.goods
        ? order.goods.map(good => ({
          name: good.name,
          price: good.price,
          image_url: good.imgList && good.imgList.length > 0 ? good.imgList[0] : null, // 取 imgList 数组的第一个值作为 image_url
        }))
        : [], // 如果没有商品信息，返回空数组
    }));

    return {
      code: 200,
      message: "success",
      data: result, // 返回修正后的订单数据
    };
  } catch (error) {
    console.error("Error querying users_orders:", error);
    return {
      code: 500,
      message: "Internal Server Error",
    };
  }
}
