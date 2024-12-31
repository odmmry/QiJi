import cloud from '@lafjs/cloud';
import { createReadStream } from 'fs';

export default async function (ctx: FunctionContext) {
  // 检查请求中是否包含文件
  if (!ctx.files || ctx.files.length === 0) {
    return {
      code: 400,
      message: '没有提供文件',
    };
  }

  try {
    const file = ctx.files[0]; // 获取第一个上传的文件
    const bucket = cloud.storage.bucket('9i87nwhg-sealaf-gi1oxe5s8w-cloud-bin'); // 获取存储桶实例

    // 定义文件名，确保唯一性
    const filename = `${Date.now()}-${file.originalname}`;

    // 读取文件流
    const fileStream = createReadStream(file.path);

    // 使用 writeFile 方法上传文件
    await bucket.writeFile(filename, fileStream, {
      ContentType: file.mimetype, // 设置文件的 MIME 类型
    });

    // 生成文件的访问 URL
    const url = bucket.externalUrl(filename);

    // 返回成功信息
    return {
      code: 200,
      message: '文件上传成功',
      data: {
        image_url: url,
      },
    };
  } catch (error) {
    console.error('文件上传失败:', error);
    return {
      code: 500,
      message: '服务器错误，请稍后重试',
    };
  }
}
