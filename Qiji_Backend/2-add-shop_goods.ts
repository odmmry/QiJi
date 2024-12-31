import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  const { name, price, desc, user_id, imgList } = ctx.body;

  if (!name || !price || !desc || !user_id) {
    return { code: 400, message: '缺少必要参数: name、price、desc 或 user_id' };
  }

  try {
    const db = cloud.database();
    const result = await db.collection('shop_goods').add({
      name,
      price,
      desc,
      user_id,
      imgList: imgList || [],
      create_time: Math.floor(Date.now() / 1000), // 11位时间戳
    });

    // 返回发布成功的商品信息
    return {
      code: 200,
      message: '商品发布成功',
      data: {
        id: result.id,
        name,
        price,
        desc,
        user_id,
        imgList: imgList || [],
        create_time: Math.floor(Date.now() / 1000),
      },
    };
  } catch (error) {
    console.error('发布商品失败:', error);
    return { code: 500, message: '服务器错误，请稍后重试' };
  }
}