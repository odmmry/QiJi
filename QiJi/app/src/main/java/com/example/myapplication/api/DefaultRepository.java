package com.example.myapplication.api;

import com.example.myapplication.activity.CreateCompetitionActivity;
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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class DefaultRepository {

    private static DefaultRepository instance;
    private final DefaultService service;

    private DefaultRepository() {
        OkHttpClient okHttpClient = NetworkModule.providerOkHttpClient();
        Retrofit retrofit = NetworkModule.provideRetrofit(okHttpClient);
        service = retrofit.create(DefaultService.class);
    }

    public synchronized static DefaultRepository getInstance() {
        if (instance == null) {
            instance = new DefaultRepository();
        }
        return instance;
    }

    public Observable<DetailResponse<LoginResponse>> login(String username, String password) {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);

        return service.login(loginRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> register(String username, String password, String nickname) {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername(username);
        registerRequest.setPassword(password);
        registerRequest.setNickname(nickname);

        return service.register(registerRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> createPost(String content, List<String> imgList, String user_id) {
        CreatePostRequest createPostRequest = new CreatePostRequest();
        createPostRequest.setContent(content);
        createPostRequest.setImgList(imgList);
        createPostRequest.setUserId(user_id);

        return service.createPost(createPostRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DetailResponse<List<PostItem>>> getPostList() {
        return service.getPostList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DetailResponse<List<PostItem>>> getPostList(String userId) {
        return service.getPostList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DetailResponse<PostItem>> getPostDetail(String _id) {
        return service.getPostDetail(_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> delPost(String _id) {
        DelPostRequest request = new DelPostRequest();
        request.set_id(_id);

        return service.delPost(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> thumbPost(String post_id, String user_id) {
        ThumbPostRequest request = new ThumbPostRequest();
        request.setPostId(post_id);
        request.setUser_id(user_id);

        return service.thumbPost(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DetailResponse<List<CommentItem>>> getPostCommentList(String post_id) {
        return service.getPostCommentList(post_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<BaseResponse> addPostComment(String post_id, String content, String user_id) {
        AddPostCommentRequest addPostCommentRequest = new AddPostCommentRequest();
        addPostCommentRequest.setPostId(post_id);
        addPostCommentRequest.setContent(content);
        addPostCommentRequest.setUser_id(user_id);


        return service.addPostComment(addPostCommentRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<BaseResponse> createGoods(String name, String price, String desc, List<String> imgList, String user_id) {
        CreateGoodsRequest request = new CreateGoodsRequest();
        request.setName(name);
        request.setPrice(price);
        request.setDesc(desc);
        request.setImgList(imgList);
        request.setUserId(user_id);

        return service.createGoods(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DetailResponse<List<ShopGoods>>> getGoodsList() {
        return service.getGoodsList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DetailResponse<ShopGoods>> getGoodsDetail(String id) {
        return service.getGoodsDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> addGoodsToCart(String goodsId, String user_id) {
        AddGoodsToCartRequest request = new AddGoodsToCartRequest();
        request.setShopGoodsId(goodsId);
        request.setUserId(user_id);

        return service.addGoodsToCart(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DetailResponse<List<Cart>>> getCartList(String userId) {
        return service.getCartList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<BaseResponse> delGoodsForCart(String cartId) {
        DelGoodsForCartRequest request = new DelGoodsForCartRequest();
        request.setCartId(cartId);

        return service.delGoodsForCart(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> submitRideLog(String mode, Long startTime, Long endTime, Float distance, String userId) {
        SubmitRideLogRequest request = new SubmitRideLogRequest();
        request.setMode(mode);
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setDistance(distance);
        request.setUserId(userId);

        return service.submitRideLog(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DetailResponse<List<Competition>>> getCompetitionList() {
        return service.getCompetitionList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> createCompetition(String type, String imageUrl, Long startDate, Long endDate, String name, String address, String desc, String userId) {
        CreateCompetitionRequest request = new CreateCompetitionRequest();
        request.setTypes(type);
        request.setImageUrl(imageUrl);
        request.setStartDate(startDate);
        request.setEndDate(endDate);
        request.setName(name);
        request.setAddress(address);
        request.setDesc(desc);
        request.setUserId(userId);

        return service.createCompetition(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DetailResponse<Competition>> getCompetitionDetail(String id, String userId) {
        return service.getCompetitionDetail(id, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> signUpCompetition1(String id, String userId) {
        SignUpCompetitionRequest request = new SignUpCompetitionRequest();
        request.setCompetitionId(id);
        request.setUserId(userId);

        return service.signUpCompetition1(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseResponse> signUpCompetition2(String id, String userId) {
        SignUpCompetitionRequest request = new SignUpCompetitionRequest();
        request.setCompetitionId(id);
        request.setUserId(userId);

        return service.signUpCompetition2(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    public Observable<BaseResponse> submitOrder(String shopGoodsId, String addressName, String addressPhone, String addressDetail, String userId) {
        SubmitOrderRequest request = new SubmitOrderRequest();
        request.setShopGoodsId(shopGoodsId);
        request.setAddressName(addressName);
        request.setAddressPhone(addressPhone);
        request.setAddressDetail(addressDetail);
        request.setUserId(userId);

        return service.submitOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<BaseResponse> submitOrder(String addressName, String addressPhone, String addressDetail, String userId) {
        SubmitOrderForCartRequest request = new SubmitOrderForCartRequest();
        request.setAddressName(addressName);
        request.setAddressPhone(addressPhone);
        request.setAddressDetail(addressDetail);
        request.setUserId(userId);

        return service.submitOrder(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<DetailResponse<List<Order>>> gerOrderList(String userId) {
        return service.gerOrderList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<DetailResponse<List<Notification>>> getNotificationList(String userId) {
        return service.getNotificationList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DetailResponse<List<RideLog>>> getRideLogList(String userId) {
        return service.getRideLogList(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<DetailResponse<UserInfo>> getUserInfo(String userId) {
        return service.getUserInfo(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    public Observable<BaseResponse> changeUserInfo(String userId, String nickname, String avatarUrl) {
        ChangeUserInfoRequest changeUserInfoRequest = new ChangeUserInfoRequest();
        changeUserInfoRequest.setUserId(userId);
        changeUserInfoRequest.setNickname(nickname);
        changeUserInfoRequest.setAvatarUrl(avatarUrl);

        return service.changeUserInfo(changeUserInfoRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<DetailResponse<List<CompetitionRanking>>> getCompetitionRankingList(String id) {
        return service.getCompetitionRankingList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<DetailResponse<UploadImageResponse>> uploadFile(MultipartBody.Part file) {
        return service.uploadFile(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
