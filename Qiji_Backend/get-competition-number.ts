import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 从请求参数中解析 competition_id
  const { competition_id } = ctx.query;

  // 验证参数是否存在
  if (!competition_id) {
    return {
      code: 400,
      message: '缺少必要参数: competition_id',
    };
  }

  try {
    const db = cloud.database();

    // 检查比赛是否存在
    const competition = await db.collection('competition').where({ _id: competition_id }).get();
    if (!competition.data.length) {
      return {
        code: 200,
        message: '比赛不存在或尚未开始',
        data: [],
      };
    }

    // 查询参赛者信息并严格筛选骑行记录
    const participantsResult = await db
      .collection('competition_participant')
      .aggregate()
      .match({ competition_id}) // 仅筛选指定 competition_id 的参赛者
      .lookup({
        from: 'users', // 连接 users 表
        localField: 'user_id',
        foreignField: '_id',
        as: 'user_info',
      })
      .unwind('$user_info') // 展开 user_info 数组
      .lookup({
        from: 'ride_log', // 连接 ride_log 表
        let: { userId: '$user_id', competitionId: '$competition_id' }, // 定义变量
        pipeline: [
          {
            $match: {
              $expr: {
                $and: [
                  { $eq: ['$user_id', '$$userId'] }, // 匹配 user_id
                  { $eq: ['$mode', '$$competitionId'] }, // 匹配 competition_id
                ],
              },
            },
          },
        ],
        as: 'ride_logs',
      })
      .unwind({
        path: '$ride_logs',
        preserveNullAndEmptyArrays: false, // 不保留没有骑行记录的参赛者
      })
      .sort({ 'ride_logs.distance': -1 }) // 根据距离降序排序
      .project({
        _id: 1,
        competition_id: 1,
        user_id: 1,
        username: '$user_info.username',
        nickname: '$user_info.nickname',
        avatar_url: '$user_info.avatar_url',
        distance: '$ride_logs.distance', // 骑行记录的距离
        create_time: '$ride_logs.create_time', // 骑行记录的创建时间
      })
      .end();

    // 返回结果
    return {
      code: 200,
      message: '查询成功',
      data: participantsResult.data, // 返回所有参赛者的骑行记录
    };
  } catch (error) {
    console.error('查询失败:', error);
    return {
      code: 500,
      message: '服务器错误，请稍后重试',
    };
  }
}