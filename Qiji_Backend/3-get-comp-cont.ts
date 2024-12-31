import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  const db = cloud.mongo.db;

  // 获取请求参数
  const { competition_id, user_id } = ctx.query;

  // 如果没有提供 competition_id，则返回错误信息
  if (!competition_id) {
    return {
      code: 400,
      message: 'Competition ID is required',
    };
  }

  try {
    // 从 competition 表中查询 _id 与 competition_id 相同的记录
    const participant = await db.collection('competition')
      .findOne({ _id: competition_id });

    // 如果没有找到记录，返回相应的信息
    if (!participant) {
      return {
        code: 404,
        message: 'No participant found for the given Competition ID',
      };
    }

    // 获取当前时间的十位时间戳
    const currentTime = Math.floor(Date.now() / 1000); // 当前时间的十位时间戳

    // 判断当前时间是否晚于 end_date
    let allow = true; // 默认允许
    let allow_ride = true;

    if (currentTime > participant.end_date) {
      allow = false; // 当前时间晚于end_date，不允许
    }

    if (currentTime > participant.end_date || currentTime < participant.start_date) {
      allow_ride = false; // 当前时间不在允许范围内
    }

    // 查询 competition_participant 表中是否存在 user_id 和 competition_id
    const userExists = await db.collection('competition_participant')
      .findOne({ user_id: user_id, competition_id: competition_id });

    // 判断 user_id 是否存在并重新设置 allow 字段
    if (userExists) {
      allow = false; // 如果 user_id 存在，设置为不允许
    }

    // 查询 ride_log 表中是否存在 mode 和 user_id
    const rideLogExists = await db.collection('ride_log')
      .findOne({ mode: competition_id, user_id: user_id });
    if (rideLogExists) {
      allow_ride = false; // 如果 user_id 在 ride_log 中存在，设置为不允许
    }
    if (!userExists) {
      allow_ride = false; // 如果 user_id 不存在，设置为不允许
    }

    // 返回查询到的结果，直接在 data 中列举 participant 的属性
    return {
      code: 200,
      data: {
        _id: participant._id,
        name: participant.name, // 示例字段，替换为实际字段
        image_url: participant.image_url, // 示例字段，替换为实际字段
        address: participant.address, // 示例字段，替换为实际字段
        desc: participant.desc,
        start_date: participant.start_date,
        end_date: participant.end_date,
        types: participant.types,
        create_time: participant.create_time,
        allow_post: allow,
        allow_ride: allow_ride, // 返回 allow_ride 字段

      }
    };
  } catch (error) {
    return {
      code: 500,
      message: 'An error occurred while fetching the participant',
      error: error.message,
    };
  }
}