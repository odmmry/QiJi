import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  const { _id } = ctx.body;

  if (!_id) {
    return {code: 400, message: '缺少必要参数: id' };
  }

  try {
    const db = cloud.database();
    const result = await db.collection('shop_goods').doc(_id).remove();

    if (result.deleted === 1) {
      return { code: 200, message: '商品删除成功' };
    } else {
      return { code: 404, message: '未找到该商品' };
    }
  } catch (error) {
    console.error('删除商品失败:', error);
    return { code: 500, message: '服务器错误，请稍后重试' };
  }
}