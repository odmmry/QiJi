import cloud from '@lafjs/cloud'

export default async function (ctx: FunctionContext) {
  const db = cloud.mongo.db;
  const competitionCollection = db.collection('competition');  // 获取competition集合

  try {
    // 查询所有竞赛数据
    const competitions = await competitionCollection.find({}).toArray();

    return {
      code: 200,
      data: competitions
    };
  } catch (error) {
    return {
      code: 500,
      message: error.message || '查询失败'
    };
  }
}