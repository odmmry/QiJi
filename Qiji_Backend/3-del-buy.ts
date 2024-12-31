import cloud from '@lafjs/cloud';

export default async function (ctx) {
  // 获取数据库实例
  const db = cloud.mongo.db;

  // 从请求上下文中获取参数
  const { cart_id } = ctx.body;

  // 检查 order_id 是否提供
  if (!cart_id) {
    return {
      code: 404,
      message: 'cart_id is required',
    };
  }

  try {
    // 从 users_cart 列表中删除指定的记录
    const result = await db.collection('users_cart').deleteOne({ _id:cart_id });

    if (result.deletedCount === 1) {
      return {
        code: 200,
        message: '成功删除',
      };
    } else {
      return {
        code: 500,
        message: '商品不存在',
      };
    }
  } catch (error) {
    // 捕获并返回错误信息
    return {
      code: 500,
      message: 'An error occurred while deleting the order item',
      error: error.message,
    };
  }
}
