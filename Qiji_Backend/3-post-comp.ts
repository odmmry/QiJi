import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  const db = cloud.database(); // 获取数据库实例

  // 获取传入的比赛信息
  const { name, image_url, address, desc, start_date, end_date, user_id,types } = ctx.body;

  // 检查必需的参数是否提供
  if (!name || !image_url || !address || !desc || !start_date || !end_date || !user_id||!types) {
    return { code: 400, message: 'Missing required fields' };
  }

  // 构造要插入到数据库的比赛数据
  const competitionData = {
    name,
    image_url,
    address,
    desc,
    start_date,
    end_date,
    user_id,
    types,
    create_time: Math.floor(Date.now() / 1000), // 11位时间戳
  };

  // 将新的比赛记录添加到 competitions 表中
  const { id, ok } = await db.collection('competition').add(competitionData); // 使用 add 方法

  if (ok) {
    // 返回成功响应
    return {
      code: 200,
      message: 'Competition created successfully',
      data: {
        ...competitionData,
        id // 返回插入的比赛数据及其 ID
      }
    };
  } else {
    return { code: 500, message: 'Failed to create competition' }; // 处理添加失败的情况
  }
}