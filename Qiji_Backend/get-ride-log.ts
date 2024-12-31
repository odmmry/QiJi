import cloud from '@lafjs/cloud';

export default async function (ctx: FunctionContext) {
  //从请求体中解析查询参数，并设置默认值
  const { ride_id, user_id } = ctx.query;

  try {
    //获取数据库实例
    const db = cloud.database();

    //初始化查询条件对象
    const queryConditions: any = {};

    //如果请求体中包含 rideId，将其添加到查询条件中
    if (ride_id) queryConditions._id = ride_id;

    //如果请求体中包含 userId，将其添加到查询条件中
    if (user_id) queryConditions.user_id = user_id;
    const result = await db
      .collection('ride_log') // 指定集合为 ride_log
      .where(queryConditions) // 按条件筛选
      .get(); // 执行查询

    


    //返回查询结果，包括数据、总记录数和成功信息
    return {
      code: 200, //HTTP 状态码 200 表示请求成功
      message: '查询成功', //返回消息
      data: result.data, //查询到的记录数据
      total: result.total //查询结果的总记录数
    };
  } catch (error) {
    //捕获查询过程中的错误，并记录到日志中
    console.error('查询失败:', error);

    //返回服务器错误信息
    return {
      code: 500, //HTTP 状态码 500 表示服务器内部错误
      message: '服务器错误，请稍后重试' //提示客户端稍后再试
    };
  }
}