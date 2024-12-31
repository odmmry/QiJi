import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 解析查询参数中的 user_id
  const { user_id } = ctx.query;

  // 验证查询参数是否包含 user_id
  if (!user_id) {
    return {
      code: 400,
      message: '缺少必要参数: user_id'
    };
  }

  try {
    const db = cloud.database();

    // 使用聚合查询，获取 competition_participant 表中与 user_id 相同的记录
    const aggregate = db.collection('competition_participant').aggregate()
      .match({ user_id }) // 使用 user_id 作为查询条件
      .lookup({
        from: 'competition', // 关联的集合名称
        localField: 'competition_id', // 本集合的关联字段
        foreignField: '_id', // 外部集合的关联字段
        as: 'competition_info' // 输出的竞争信息字段名称
      })
      .project({
        user_id: 1,
        competition_id: 1,
        competition_info: { $first: '$competition_info' }, // 仅取第一个竞争信息
      });

    const result = await aggregate.end();

    if (!result.data || result.data.length === 0) {
      return {
        code: 404,
        message: '未找到对应的参与记录'
      };
    }

    // 返回查询结果，并确保返回的数据结构符合要求
    return {
      code: 200,
      message: '查询成功',
      data: result.data.map(participant => ({
        user_id: participant.user_id,
        competition_id: participant.competition_id,
        name: participant.competition_info ? participant.competition_info.name : null,
        address: participant.competition_info ? participant.competition_info.address : null,
        create_time: participant.competition_info ? participant.competition_info.create_time:null,
      }))
    };
  } catch (error) {
    console.error('查询失败:', error);
    return {
      code: 500,
      message: '服务器错误，请稍后重试'
    };
  }
}