package com.example.myapplication.api;

import com.example.myapplication.model.Cart;
import com.example.myapplication.model.CommentItem;
import com.example.myapplication.model.Competition;
import com.example.myapplication.model.CompetitionRanking;
import com.example.myapplication.model.Notification;
import com.example.myapplication.model.Order;
import com.example.myapplication.model.PostItem;
import com.example.myapplication.model.RideLog;
import com.example.myapplication.model.ShopGoods;
import com.example.myapplication.model.UserInfo;
import com.example.myapplication.model.request.AddGoodsToCartRequest;
import com.example.myapplication.model.request.AddPostCommentRequest;
import com.example.myapplication.model.request.ChangeUserInfoRequest;
import com.example.myapplication.model.request.CreateCompetitionRequest;
import com.example.myapplication.model.request.CreateGoodsRequest;
import com.example.myapplication.model.request.CreatePostRequest;
import com.example.myapplication.model.request.DelGoodsForCartRequest;
import com.example.myapplication.model.request.DelPostRequest;
import com.example.myapplication.model.request.LoginRequest;
import com.example.myapplication.model.request.RegisterRequest;
import com.example.myapplication.model.request.SignUpCompetitionRequest;
import com.example.myapplication.model.request.SubmitOrderForCartRequest;
import com.example.myapplication.model.request.SubmitOrderRequest;
import com.example.myapplication.model.request.SubmitRideLogRequest;
import com.example.myapplication.model.request.ThumbPostRequest;
import com.example.myapplication.model.response.BaseResponse;
import com.example.myapplication.model.response.DetailResponse;
import com.example.myapplication.model.response.LoginResponse;
import com.example.myapplication.model.response.UploadImageResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface DefaultService {

    // 登录
    @POST("user-logging")
    Observable<DetailResponse<LoginResponse>> login(@Body LoginRequest request);

    // 注册
    @POST("user-registration")
    Observable<BaseResponse> register(@Body RegisterRequest request);

    // 查询帖子列表
    @GET("get-postslist")
    Observable<DetailResponse<List<PostItem>>> getPostList();

    // 查询帖子列表-根据用户id
    @GET("get-postslist")
    Observable<DetailResponse<List<PostItem>>> getPostList(@Query("user_id") String user_id);

    // 查询帖子详情
    @GET("get-posts")
    Observable<DetailResponse<PostItem>> getPostDetail(@Query("_id") String _id);
    // 删帖子
    @POST("delete-posts")
    Observable<BaseResponse> delPost(@Body DelPostRequest request);

    // 发评论
    @POST("2-update-posts_thumb")
    Observable<BaseResponse> thumbPost(@Body ThumbPostRequest request);

    // 发帖子
    @POST("post-posts")
    Observable<BaseResponse> createPost(@Body CreatePostRequest request);

    // 查询帖子评论列表
    @GET("get-posts_comment-list")
    Observable<DetailResponse<List<CommentItem>>> getPostCommentList(@Query("post_id") String post_id);

    // 发评论
    @POST("2-add-posts_comment")
    Observable<BaseResponse> addPostComment(@Body AddPostCommentRequest request);


    // 发商品
    @POST("2-add-shop_goods")
    Observable<BaseResponse> createGoods(@Body CreateGoodsRequest request);


    // 查询商品列表
    @GET("2-query-shop_goods")
    Observable<DetailResponse<List<ShopGoods>>> getGoodsList();

    // 查询商品详情
    @GET("2-query-shop_goods_detail")
    Observable<DetailResponse<ShopGoods>> getGoodsDetail(@Query("_id") String id);

    // 把商品加入购物车
    @POST("2-add-users_cart")
    Observable<BaseResponse> addGoodsToCart(@Body AddGoodsToCartRequest request);


    // 查询购物车列表
    @GET("3-get-buy")
    Observable<DetailResponse<List<Cart>>> getCartList(@Query("user_id") String user_id);


    // 把商品从购物车删除
    @POST("3-del-buy")
    Observable<BaseResponse> delGoodsForCart(@Body DelGoodsForCartRequest request);


    // 提交骑行记录
    @POST("post-ride_log")
    Observable<BaseResponse> submitRideLog(@Body SubmitRideLogRequest request);

    // 查询比赛列表
    @GET("3-get-comp")
    Observable<DetailResponse<List<Competition>>> getCompetitionList();

    // 查询比赛详情
    @GET("3-get-comp-cont")
    Observable<DetailResponse<Competition>> getCompetitionDetail(@Query("competition_id") String competition_id, @Query("user_id") String user_id);

    // 创建活动
    @POST("3-post-comp")
    Observable<BaseResponse> createCompetition(@Body CreateCompetitionRequest request);


    // 报名活动-线上活动
    @POST("post-cp1")
    Observable<BaseResponse> signUpCompetition1(@Body SignUpCompetitionRequest request);
    // 报名活动-线下活动
    @POST("post-cp2")
    Observable<BaseResponse> signUpCompetition2(@Body SignUpCompetitionRequest request);



    // 商城下单-直接下单
    @POST("post-buy")
    Observable<BaseResponse> submitOrder(@Body SubmitOrderRequest request);
    // 商城下单-购物车下单
    @POST("post-buy-by-car")
    Observable<BaseResponse> submitOrder(@Body SubmitOrderForCartRequest request);
    // 查询订单列表
    @GET("3-get-order")
    Observable<DetailResponse<List<Order>>> gerOrderList(@Query("user_id") String user_id);


    // 查询消息列表
    @GET("information")
    Observable<DetailResponse<List<Notification>>> getNotificationList(@Query("user_id") String user_id);


    // 查询骑行记录
    @GET("get-ride-log")
    Observable<DetailResponse<List<RideLog>>> getRideLogList(@Query("user_id") String user_id);



    // 查询个人资料
    @GET("get-user")
    Observable<DetailResponse<UserInfo>> getUserInfo(@Query("user_id") String user_id);
    // 修改个人资料
    @POST("post-change")
    Observable<BaseResponse> changeUserInfo(@Body ChangeUserInfoRequest request);


    // 查询骑行记录
    @GET("get-competition-number")
    Observable<DetailResponse<List<CompetitionRanking>>> getCompetitionRankingList(@Query("competition_id") String competition_id);


    // 上传单个文件
    @Multipart
    @POST("picture-post")
    Observable<DetailResponse<UploadImageResponse>> uploadFile(@Part MultipartBody.Part file);
}
