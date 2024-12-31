import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  const db = cloud.database();
  const participantsCollection = db.collection('competition_participant');

  // 获取请求体中的user_id和competition_id
  const { user_id, competition_id } = ctx.body;

  try {
    // 检查是否已经存在相同的记录
    const existingParticipant = await participantsCollection
      .where({ user_id, competition_id })
      .count();

    if (existingParticipant.total > 0) {
      return {
        code: 400,
        message: '该用户已经参加过该比赛'
      };
    }

    // 插入新的记录，时间使用毫秒时间戳
    const { id, ok } = await participantsCollection.add({
      user_id,
      competition_id,
      create_time: Date.now()  // 记录当前时间戳
    });

    if (ok) {
      return {
        code: 200,
        message: '成功添加参赛者',
        data: { id, user_id, competition_id, create_time: Date.now() }
      };
    } else {
      throw new Error('插入失败');
    }
  } catch (error) {
    return {
      code: 500,
      message: error.message || '插入失败'
    };
  }
}