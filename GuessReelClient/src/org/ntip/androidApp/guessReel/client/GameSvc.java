package org.ntip.androidApp.guessReel.client;

import org.ntip.androidApp.guessReel.client.oauth.SecuredRestBuilder;
import org.ntip.androidApp.guessReel.client.ui.LoginScreenActivity;
import org.ntip.androidApp.guessReel.client.unsafe.EasyHttpClient;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;
import android.content.Intent;

public class GameSvc {
	public static final String CLIENT_ID = "mobile";
	private static GameSvcApi gameSvc_;

	public static synchronized GameSvcApi getOrShowLogin(Context ctx) {
		if (gameSvc_ != null) {
			return gameSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized GameSvcApi init(String server, String user,
			String pass) {

		gameSvc_ = new SecuredRestBuilder()
				.setLoginEndpoint(server + GameSvcApi.TOKEN_PATH)
				.setUsername(user)
				.setPassword(pass)
				.setClientId(CLIENT_ID)
				.setClient(
						new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(GameSvcApi.class);

		return gameSvc_;
	}

}
