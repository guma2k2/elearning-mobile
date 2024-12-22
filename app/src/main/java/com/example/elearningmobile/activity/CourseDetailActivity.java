package com.example.elearningmobile.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elearningmobile.R;
import com.example.elearningmobile.adapter.CartRecycleAdapter;
import com.example.elearningmobile.adapter.CurriculumRecycleAdapter;
import com.example.elearningmobile.adapter.ReviewRecycleAdapter;
import com.example.elearningmobile.adapter.StringRecycleAdapter;
import com.example.elearningmobile.api.CartApi;
import com.example.elearningmobile.api.CourseApi;
import com.example.elearningmobile.api.CreateOrder;
import com.example.elearningmobile.api.OrderApi;
import com.example.elearningmobile.api.PaymentApi;
import com.example.elearningmobile.api.ReviewApi;
import com.example.elearningmobile.api.learning.MyLearningCourseApi;
import com.example.elearningmobile.model.CourseDetailType;
import com.example.elearningmobile.model.PageableData;
import com.example.elearningmobile.model.PaymentPostVM;
import com.example.elearningmobile.model.cart.CartListGetVM;
import com.example.elearningmobile.model.course.CourseVM;
import com.example.elearningmobile.model.learning.LearningCourseVM;
import com.example.elearningmobile.model.order.OrderDetailPostDto;
import com.example.elearningmobile.model.order.OrderPostDto;
import com.example.elearningmobile.model.review.ReviewVM;
import com.example.elearningmobile.model.section.SectionVM;
import com.example.elearningmobile.ultity.DataManager;
import com.example.elearningmobile.ultity.DateFormatter;
import com.example.elearningmobile.ultity.PriceFormatter;
import com.example.elearningmobile.variable.GlobalVariable;
import com.squareup.picasso.Picasso;
import com.vnpay.authentication.VNP_AuthenticationActivity;
import com.vnpay.authentication.VNP_SdkCompletedCallback;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CourseDetailActivity extends AppCompatActivity {

    TextView tv_courseTitle_courseDetail, tv_courseDesc_courseDetail, tv_courseRating_courseDetail
            ,tv_coursePrice_courseDetail, tv_ratingCount_courseDetail, tv_courseCountInstructor_courseDetail
            ,tv_instructorName_courseDetail, tv_ratingInstructor_courseDetail, tv_reviewCountInstructor_courseDetail
            ,tv_studentCountInstructor_courseDetail, tv_courseHeadline_courseDetail, tv_courseAvgRating,
            tv_requirementTitle, tv_courseObjectiveTitle, tv_totalDurationCourse_courseDetail, tv_courseLevel_courseDetail,
            tv_totalLectureCourse_courseDetail, tv_totalSections_courseDetail, tv_totalLectures_courseDetail, tv_totalDurationCurriculum_courseDetail;
    RatingBar rb_courseRating_courseDetail;
    RecyclerView rc_objectives_courseDetail, rc_curriculum_courseDetail, rc_reviews_courseDetail,
            rc_requirements_courseDetail;
    private Long orderId = null;

    ImageView iv_courseImage_courseDetail, iv_instructorPhoto_courseDetail;

    private Long courseId;

    private CourseVM course = new CourseVM();

    private StringRecycleAdapter objectiveRecycleAdapter, requirementRecycleAdapter;

    private CurriculumRecycleAdapter curriculumRecycleAdapter;
    private ReviewRecycleAdapter reviewRecycleAdapter;

    private List<ReviewVM> reviewVMList = new ArrayList<>();


    private List<String> requirements = new ArrayList<>();

    private List<String> objectives = new ArrayList<>();

    private Button btn_backCourseDetail, btn_buy_courseDetail, btn_goToLearning, btn_addToCart_courseDetail;


    private List<SectionVM> sectionVMS = new ArrayList<>();
    Boolean isBought = false;
    Long isInCart = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        setControl();
        setEvent();
    }


    public void setSectionVMS(List<SectionVM> sectionVMList) {
        curriculumRecycleAdapter.notifyDataSetChanged();
    }

    private void setEvent() {
        GlobalVariable globalVariable = (GlobalVariable) getApplication();

        StrictMode.ThreadPolicy policy = new
                StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX);
        // bind components with ids
        // handle CreateOrder
        btn_buy_courseDetail.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                showConfirmPayment(globalVariable);
            }
        });

        btn_addToCart_courseDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalVariable globalVariable = (GlobalVariable) getApplication();
                if (globalVariable.isLoggedIn()) {
                    String token = globalVariable.getAccess_token();
                    String bearerToken = "Bearer " + token;
                    if (isInCart != null) {
                        CartApi.cartApi.deleteCart(bearerToken, isInCart).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                Toast.makeText(getApplicationContext(), "Xóa khỏi giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                btn_goToLearning.setVisibility(View.GONE);
                                btn_buy_courseDetail.setVisibility(View.VISIBLE);
                                btn_addToCart_courseDetail.setText("Thêm vào giỏ hàng");
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT);
                            }
                        });

                    } else {
                        CartApi.cartApi.addToCart(bearerToken, courseId).enqueue(new Callback<CartListGetVM>() {
                            @Override
                            public void onResponse(Call<CartListGetVM> call, Response<CartListGetVM> response) {
                                Toast.makeText(getApplicationContext(), "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                btn_goToLearning.setVisibility(View.GONE);
                                btn_buy_courseDetail.setVisibility(View.VISIBLE);
                                btn_addToCart_courseDetail.setText("Xóa khỏi giỏ hàng");
                            }

                            @Override
                            public void onFailure(Call<CartListGetVM> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT);
                            }
                        });
                    }
                } else {
                    redirectToLoginPage();
                }

            }
        });




        reviewRecycleAdapter = new ReviewRecycleAdapter(reviewVMList);
        rc_reviews_courseDetail.setAdapter(reviewRecycleAdapter);
        rc_reviews_courseDetail.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        objectiveRecycleAdapter = new StringRecycleAdapter(objectives, CourseDetailType.objective);
        rc_objectives_courseDetail.setAdapter(objectiveRecycleAdapter);
        rc_objectives_courseDetail.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        requirementRecycleAdapter = new StringRecycleAdapter(requirements, CourseDetailType.requirement);
        rc_requirements_courseDetail.setAdapter(requirementRecycleAdapter);
        rc_requirements_courseDetail.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


        curriculumRecycleAdapter = new CurriculumRecycleAdapter(sectionVMS, this);
        rc_curriculum_courseDetail.setAdapter(curriculumRecycleAdapter);
        rc_curriculum_courseDetail.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));





        btn_backCourseDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToHomePage();
            }
        });
    }

    private void showConfirmPayment(GlobalVariable globalVariable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận thanh toán")
                .setMessage("Bạn có muốn thực hiện thanh toán này?")
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (globalVariable.isLoggedIn()) {
                            String jwtToken = globalVariable.getAccess_token();
                            String bearerToken = "Bearer " + jwtToken;

                            OrderPostDto orderPostDto = new OrderPostDto();

                            List<OrderDetailPostDto> orderDetailPostDtos = new ArrayList<>();
                            OrderDetailPostDto orderDetailPostDto = new OrderDetailPostDto();
                            orderDetailPostDto.setPrice(course.getPrice());
                            orderDetailPostDto.setCourseId(course.getId());
                            orderDetailPostDtos.add(orderDetailPostDto);

                            orderPostDto.setOrderDetails(orderDetailPostDtos);

                            OrderApi.orderApi.createOrder(bearerToken, orderPostDto).enqueue(new Callback<Long>() {
                                @Override
                                public void onResponse(Call<Long> call, Response<Long> response) {
                                    if (response.isSuccessful()) {
                                        orderId = response.body();
                                        openZaloPay(bearerToken);
                                    }
                                }

                                @Override
                                public void onFailure(Call<Long> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT);
                                }
                            });

                        } else {
                            redirectToLoginPage();
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle "Cancel" button click
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void openZaloPay(String bearerToken) {
        CreateOrder orderApi = new CreateOrder();

        try {
            JSONObject data = orderApi.createOrder(course.getPrice().toString());
            String code = data.getString("return_code");

            if (code.equals("1")) {
                String token = data.getString("zp_trans_token");
                ZaloPaySDK.getInstance().payOrder(CourseDetailActivity.this, token, "demozpdk://app", new PayOrderListener() {
                    @Override
                    public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                        PaymentPostVM paymentPostVM = new PaymentPostVM();
                        paymentPostVM.setAmount(course.getPrice());
                        paymentPostVM.setBankCode(transactionId);
                        paymentPostVM.setBankTranNo(transToken);
                        paymentPostVM.setCardType("Zalopay");
                        if (orderId != null) {
                            paymentPostVM.setOrderId(orderId);
                        }
                        paymentPostVM.setPayDate(DateFormatter.getCurrentDateTime());

                        PaymentApi.paymentApi.createPaymentSuccess(bearerToken, paymentPostVM).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    redirectToResult(200, "Chúc mừng bạn đã thanh toán thành công");
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });


                    }

                    @Override
                    public void onPaymentCanceled(String zpTransToken, String appTransID) {
                        redirectToResult(400, "Thanh toán thất bại");
                        OrderApi.orderApi.updateOrderStatus(bearerToken, orderId, "FAILURE").enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                        redirectToResult(400, zaloPayError.toString());

                        OrderApi.orderApi.updateOrderStatus(bearerToken, orderId, "FAILURE").enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void redirectToResult(int status, String message) {
        Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("status", status);
        bundle.putString("message", message);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    private void redirectToHomePage() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("fragment", R.id.nav_home);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void redirectToLoginPage () {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        courseId = DataManager.getCourseId();
        setCourse();
        setReviews();
    }

    private void setCourse () {
        if (courseId != null) {
            GlobalVariable globalVariable = (GlobalVariable) getApplication();

            if (globalVariable.isLoggedIn()){
                String token = globalVariable.getAccess_token();
                String bearerToken = "Bearer " + token;

                MyLearningCourseApi.myLearningCourseApi.getByStudent(bearerToken).enqueue(new Callback<List<LearningCourseVM>>() {
                    @Override
                    public void onResponse(Call<List<LearningCourseVM>> call, Response<List<LearningCourseVM>> response) {
                        List<LearningCourseVM> res = response.body();

                        for (LearningCourseVM learningCourseVM : res) {
                            if (learningCourseVM.getCourse().getId().equals(courseId)) {
                                isBought = true;
                                break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<LearningCourseVM>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT);
                    }
                });
                if (isBought) {
                    btn_goToLearning.setVisibility(View.VISIBLE);
                    btn_buy_courseDetail.setVisibility(View.GONE);
                    btn_addToCart_courseDetail.setVisibility(View.GONE);
                } else {
                    CartApi.cartApi.getCarts(bearerToken).enqueue(new Callback<List<CartListGetVM>>() {
                        @Override
                        public void onResponse(Call<List<CartListGetVM>> call, Response<List<CartListGetVM>> response) {
                            List<CartListGetVM> res = response.body();
                            for (CartListGetVM cartListGetVM : res) {
                                if (cartListGetVM.getCourse().getId().equals(courseId)) {
                                    isInCart = cartListGetVM.getId();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<CartListGetVM>> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT);
                        }
                    });

                    if (isInCart != null) {
                        btn_goToLearning.setVisibility(View.GONE);
                        btn_buy_courseDetail.setVisibility(View.VISIBLE);
                        btn_addToCart_courseDetail.setText("Xóa khỏi giỏ hàng");
                    } else {
                        btn_goToLearning.setVisibility(View.GONE);
                        btn_buy_courseDetail.setVisibility(View.VISIBLE);
                        btn_addToCart_courseDetail.setText("Thêm vào giỏ hàng");
                    }
                }
            } else {
                btn_goToLearning.setVisibility(View.GONE);
                btn_buy_courseDetail.setVisibility(View.VISIBLE);
                btn_addToCart_courseDetail.setText("Thêm vào giỏ hàng");
            }
            CourseApi.courseApi.getCourseById(courseId).enqueue(new Callback<CourseVM>() {
                @Override
                public void onResponse(Call<CourseVM> call, Response<CourseVM> response) {
                    if (response.body() != null) {
                        CourseVM body = response.body();
                        course = body;
                        if (course.getId() != null) {
                            tv_courseTitle_courseDetail.setText(course.getTitle());
                            tv_courseDesc_courseDetail.setText(Html.fromHtml(course.getDescription()));
                            tv_courseRating_courseDetail.setText(course.getAverageRating()+"");
                            rb_courseRating_courseDetail.setRating((float) course.getAverageRating());
                            tv_ratingCount_courseDetail.setText(String.format("%d đánh giá", course.getRatingCount()));
                            tv_coursePrice_courseDetail.setText(PriceFormatter.formatPriceInVND(course.getPrice()));
                            Picasso.get().load(course.getUser().getPhoto()).into(iv_instructorPhoto_courseDetail);
                            tv_instructorName_courseDetail.setText(course.getUser().getFullName());
                            tv_ratingInstructor_courseDetail.setText(String.format("%s xếp hạng giảng viên", course.getUser().getAverageRating() + ""));
                            tv_reviewCountInstructor_courseDetail.setText(String.format("%d đánh giá", course.getUser().getNumberOfReview()));
                            tv_studentCountInstructor_courseDetail.setText(String.format("%d học viên", course.getUser().getNumberOfStudent()));
                            tv_courseCountInstructor_courseDetail.setText(String.format("%d khóa học", course.getUser().getNumberOfCourse()));
                            tv_courseHeadline_courseDetail.setText(course.getHeadline());
                            tv_courseAvgRating.setText(String.format("%s xếp hạng khóa học", course.getAverageRating() + ""));

                            tv_totalDurationCourse_courseDetail.setText(String.format("Thời lượng %s", course.getTotalDurationCourse()));
                            tv_courseLevel_courseDetail.setText(String.format("Cấp độ ", course.getLevel()));
                            tv_totalLectureCourse_courseDetail.setText(String.format("%d bài học", course.getTotalLectureCourse()));

                            tv_totalSections_courseDetail.setText(String.format("%d phan", course.getSections().size()));
                            tv_totalLectures_courseDetail.setText(String.format("%d bài giảng", course.getTotalLectureCourse()));
                            tv_totalDurationCurriculum_courseDetail.setText(course.getTotalDurationCourse());



                            if (course.getRequirements() == null || course.getRequirements().length == 0) {
                                tv_requirementTitle.setVisibility(View.GONE);
                                rc_requirements_courseDetail.setVisibility(View.GONE);
                            }else {
                                tv_requirementTitle.setVisibility(View.VISIBLE);
                                rc_requirements_courseDetail.setVisibility(View.VISIBLE);
                                requirements.clear();
                                List<String> requirementList = new ArrayList<>();
                                for(String value : course.getRequirements()) {
                                    requirementList.add(value);
                                }
                                requirements.addAll(requirementList);
                            }

                            if (course.getObjectives() == null || course.getObjectives().length == 0  ) {
                                tv_courseObjectiveTitle.setVisibility(View.GONE);
                                rc_objectives_courseDetail.setVisibility(View.GONE);
                            }else {
                                tv_courseObjectiveTitle.setVisibility(View.VISIBLE);
                                rc_objectives_courseDetail.setVisibility(View.VISIBLE);
                                objectives.clear();
                                List<String> values = new ArrayList<>();
                                for(String value : course.getObjectives()) {
                                    values.add(value);
                                }
                                objectives.addAll(values);
                            }

                        }


                        List<SectionVM> sectionVMList = body.getSections();
                        sectionVMS.clear();
                        sectionVMS.addAll(sectionVMList);
                        curriculumRecycleAdapter.notifyDataSetChanged();
                        objectiveRecycleAdapter.notifyDataSetChanged();
                        requirementRecycleAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<CourseVM> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra trong quá trình đăng nhap", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setReviews() {
        if (courseId != null) {
            ReviewApi.reviewApi.getCourseById(courseId).enqueue(new Callback<PageableData<ReviewVM>>() {
                @Override
                public void onResponse(Call<PageableData<ReviewVM>> call, Response<PageableData<ReviewVM>> response) {
                    if (response.body() != null) {
                        List<ReviewVM> reviewVMS = response.body().getContent();
                        if (reviewVMS.size() > 0 ) {
                            reviewVMList.clear();
                            reviewVMList.addAll(reviewVMS);
                            reviewRecycleAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(Call<PageableData<ReviewVM>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Có lỗi xảy ra trong quá trình đăng nhap", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setControl() {
        tv_courseTitle_courseDetail = findViewById(R.id.tv_courseTitle_courseDetail);
        tv_courseDesc_courseDetail = findViewById(R.id.tv_courseDesc_courseDetail);
        tv_courseRating_courseDetail = findViewById(R.id.tv_courseRating_courseDetail);
        tv_coursePrice_courseDetail = findViewById(R.id.tv_coursePrice_courseDetail);
        rb_courseRating_courseDetail = findViewById(R.id.rb_courseRating_courseDetail);
        rc_objectives_courseDetail = findViewById(R.id.rc_objectives_courseDetail);
        rc_curriculum_courseDetail = findViewById(R.id.rc_curriculum_courseDetail);
        iv_courseImage_courseDetail = findViewById(R.id.iv_courseImage_courseDetail);
        tv_ratingCount_courseDetail = findViewById(R.id.tv_ratingCount_courseDetail);
        tv_instructorName_courseDetail = findViewById(R.id.tv_instructorName_courseDetail);
        tv_ratingInstructor_courseDetail = findViewById(R.id.tv_ratingInstructor_courseDetail);
        tv_reviewCountInstructor_courseDetail = findViewById(R.id.tv_reviewCountInstructor_courseDetail);
        tv_studentCountInstructor_courseDetail = findViewById(R.id.tv_studentCountInstructor_courseDetail);
        rc_requirements_courseDetail = findViewById(R.id.rc_requirements_courseDetail);
        iv_instructorPhoto_courseDetail = findViewById(R.id.iv_instructorPhoto_courseDetail);
        rc_reviews_courseDetail = findViewById(R.id.rc_reviews_courseDetail);
        btn_backCourseDetail = findViewById(R.id.btn_backCourseDetail);
        btn_buy_courseDetail = findViewById(R.id.btn_buy_courseDetail);
        tv_courseHeadline_courseDetail = findViewById(R.id.tv_courseHeadline_courseDetail);
        tv_courseCountInstructor_courseDetail = findViewById(R.id.tv_courseCountInstructor_courseDetail);
        tv_courseAvgRating = findViewById(R.id.tv_courseAvgRating);
        tv_requirementTitle = findViewById(R.id.tv_requirementTitle);
        tv_courseObjectiveTitle = findViewById(R.id.tv_courseObjectiveTitle);
        tv_totalDurationCourse_courseDetail = findViewById(R.id.tv_totalDurationCourse_courseDetail);
        tv_courseLevel_courseDetail = findViewById(R.id.tv_courseLevel_courseDetail);
        tv_totalLectureCourse_courseDetail = findViewById(R.id.tv_totalLectureCourse_courseDetail);
        btn_addToCart_courseDetail = findViewById(R.id.btn_addToCart_courseDetail);
        btn_goToLearning = findViewById(R.id.btn_goToLearning);


        tv_totalSections_courseDetail = findViewById(R.id.tv_totalSections_courseDetail);
        tv_totalLectures_courseDetail = findViewById(R.id.tv_totalLectures_courseDetail);
        tv_totalDurationCurriculum_courseDetail = findViewById(R.id.tv_totalDurationCurriculum_courseDetail);
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            courseId = extras.getLong("courseId");
//            System.out.println(courseId);
//        } else {
//            courseId = null;
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
    }
}