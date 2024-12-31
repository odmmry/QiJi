import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  // 从请求体中解析必要参数
  const { mode, start_time, end_time, distance, user_id } = ctx.body;

  // 验证请求体是否包含必填参数
  if (!start_time || !end_time || !user_id) {
    return {
      code: 400,
      message: '缺少必要参数: start_time、end_time、distance 或 user_id'
    };
  }

  const db = cloud.database();

  // 验证mode的有效性，mode 必须是字符串类型
  const validModes = ['1', '2', '3']; // 修改为字符串数组
  if (!validModes.includes(mode)) {
    const competitionCount = await db.collection('competition')
      .where({ _id: mode }) // 确保这里的 mode 仍然是字符串类型
      .count();

    if (competitionCount.total === 0) {
      return {
        code: 400,
        message: '无效的 mode_id: 必须是 "1"、"2"、"3" 或 competition 表中的有效 ID'
      };
    }

    // 如果mode是competition表的id，检查时间范围
    const competitionRecord = await db.collection('competition')
      .doc(mode)
      .get();

    if (competitionRecord.ok) {
      const { start_date, end_date } = competitionRecord.data;

      // 检查 start_time 和 end_time 是否在比赛时间范围内
      if (start_time < start_date || end_time > end_date) {
        return {
          code: 400,
          message: '不在比赛时间内'
        };
      }
    }
  }

  // 确保 distance 是 float 类型
  const parsedDistance = parseFloat(distance);
  if (isNaN(parsedDistance) || parsedDistance < 0) {
    return {
      code: 400,
      message: '无效的距离，必须是一个正的数字'
    };
  }

  try {
    // 如果mode是competition表的id，检查是否已经有相同记录
    if (!validModes.includes(mode)) {
      const existingRecord = await db.collection('ride_log')
        .where({ mode: mode, user_id: user_id })
        .count();

      if (existingRecord.total > 0) {
        return {
          code: 400,
          message: '骑行记录已存在: 相同的 mode 和 user_id 组合'
        };
      }
    }

    // 调用数据库方法新增骑行记录
    const result = await db.collection('ride_log').add({
      mode: mode,
      start_time, // 开始时间戳
      end_time, // 结束时间戳
      distance: parsedDistance || 0, // 距离，确保是 float 型
      user_id, // 用户ID
      create_time: new Date().getTime() // 设置创建时间为当前时间的时间戳
    });

    // 返回新增记录的 ID
    return {
      code: 200,
      message: '骑行记录添加成功',
      data: result.id
    };
  } catch (error) {
    // 捕获错误并返回错误信息
    console.error('骑行记录添加失败:', error);
    return {
      code: 500,
      message: '服务器错误，请稍后重试'
    };
  }
}