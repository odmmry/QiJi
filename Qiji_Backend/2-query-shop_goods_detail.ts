import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  const { _id } = ctx.query;

  if (!_id) {
    return { code: 400, message: '缺少必要参数: id' };
  }

  try {
    const db = cloud.database();
    const goods = await db.collection('shop_goods').doc(_id).get();

    if (goods.data) {
      return { code: 200, message: '查询成功', data: goods.data };
    } else {
      return { code: 404, message: '未找到该商品' };
    }
  } catch (error) {
    console.error('查询商品详情失败:', error);
    return { code: 500, message: '服务器错误，请稍后重试' };
  }
}
