// 接口类型: DELETE
import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 解析查询参数中的帖子 _id
  const { _id } = ctx.body;

  // 验证查询参数是否包含帖子 _id
  if (!_id) {
    return {
      code: 400,
      message: '缺少必要参数: _id'
    };
  }

  try {
    const db = cloud.database();

    // 调用数据库方法删除指定的帖子
    const result = await db.collection('posts').doc(_id).remove();

    if (result.deleted === 0) {
      return {
        code: 404,
        message: '未找到对应的帖子，删除失败'
      };
    }

    // 返回成功删除的信息
    return {
      code: 200,
      message: '帖子删除成功',
      data: { deletedCount: result.deleted }
    };
  } catch (error) {
    console.error('帖子删除失败:', error);
    return {
      code: 500,
      message: '服务器错误，请稍后重试'
    };
  }
}