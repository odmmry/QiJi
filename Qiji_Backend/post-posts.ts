// 接口类型: POST
import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 解析请求体中的必要参数
  const { content, user_id, imgList } = ctx.body;

  // 验证请求体是否包含必填参数
  if (!content || !user_id) {
    return {
      code: 400,
      message: '缺少必要参数: content 或 user_id'
    };
  }

  // 验证 imgList 是否为数组（如果传递了 imgList）
  if (imgList && !Array.isArray(imgList)) {
    return {
      code: 400,
      message: 'imgList 必须是一个数组'
    };
  }

  try {
    const db = cloud.database();

    // 调用数据库方法新增帖子记录，并使用给定的数据结构
    const result = await db.collection('posts').add({
      content,              // 帖子内容
      user_id,              // 发布者用户ID
      imgList: imgList || [], // 图片列表，默认为空数组
      is_thumb:false,
      create_time: Date.now() // 使用当前时间戳作为创建时间
    });

    // 返回新增记录的 ID 和其他信息（如果需要）
    return {
      code: 200,
      message: '帖子添加成功',
      data: {
        _id: result.id,
        content,
        user_id,
        imgList: imgList || [], // 返回保存的图片列表
        create_time: Date.now(),
       
      }
    };
  } catch (error) {
    console.error('帖子添加失败:', error);
    return {
      code: 500,
      message: '服务器错误，请稍后重试'
    };
  }
}
