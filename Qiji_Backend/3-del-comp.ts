import cloud from '@lafjs/cloud'

export default async function (ctx: FunctionContext) {
  const db = cloud.mongo.db;

  // 获取删除请求的数据
  const { competition_id } = ctx.body; // 或者 { name } 来指定删除的比赛

  if (!competition_id) {
    return { code: 400, message: 'Missing competition_id' };
  }

  try {
    // 删除指定的 competition 数据
    const result = await db.collection('competition').deleteOne({ _id: competition_id });

    if (result.deletedCount === 1) {
      return { code: 200, message: 'Competition deleted successfully' };
    } else {
      return { code: 404, message: 'Competition not found' };
    }
  } catch (error) {
    return { code: 500, message: 'Server error', error: error.message };
  }
}
