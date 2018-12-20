package io.eblock.eos4j.api.utils;

import com.socks.library.KLog;
import com.stratagile.qlink.utils.ToastUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;

import io.eblock.eos4j.api.exception.ApiError;
import io.eblock.eos4j.api.exception.ApiException;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author mc_q776355102
 * 
 * since：2018年10月11日 下午2:26:24
 */
public class Generator {

	private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

	private static Retrofit.Builder builder = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create());

	private static Retrofit retrofit;

	public static <S> S createService(Class<S> serviceClass, String baseUrl) {
		builder.baseUrl(baseUrl);
		builder.client(httpClient.build());
		builder.addConverterFactory(GsonConverterFactory.create());
		retrofit = builder.build();
		return retrofit.create(serviceClass);
	}

	public static <T> T executeSync(Call<T> call) {
		try {
			Response<T> response = call.execute();
			if (response.isSuccessful()) {
				return response.body();
			} else {
				ApiError apiError = getApiError(response);
				KLog.i(apiError.getMessage());
				ToastUtil.displayShortToast(apiError.getMessage());
				KLog.i(Arrays.asList(apiError.getError().getDetails()));
				throw new ApiException(apiError);
			}
		} catch (IOException e) {
			ToastUtil.displayShortToast("EOS Transaction error");
			throw new ApiException(e);
		}
	}

	private static ApiError getApiError(Response<?> response) throws IOException, ApiException {
		return (ApiError) retrofit.responseBodyConverter(ApiError.class, new Annotation[0]).convert(response.errorBody());
	}
}
