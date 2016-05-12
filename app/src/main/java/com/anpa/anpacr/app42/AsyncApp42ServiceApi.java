package com.anpa.anpacr.app42;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.anpa.anpacr.common.Constants;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.game.Game;
import com.shephertz.app42.paas.sdk.android.imageProcessor.ImageProcessorService;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadFileType;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;
import com.shephertz.app42.paas.sdk.android.user.User;

public class AsyncApp42ServiceApi {
	private StorageService storageService;
	private UploadService uploadService;
	private ImageProcessorService imageService;
	
	private static AsyncApp42ServiceApi mInstance = null;
	
	private AsyncApp42ServiceApi(Context context) {
		App42API.initialize(context, Constants.App42ApiKey, Constants.App42ApiSecret);
		this.storageService = App42API.buildStorageService();
		this.uploadService = App42API.buildUploadService();
		this.imageService = App42API.buildImageProcessorService();
	}

	/*
	 * instance of class
	 */
	public static AsyncApp42ServiceApi instance(Context context) {

		if (mInstance == null) {
			mInstance = new AsyncApp42ServiceApi(context);
		}

		return mInstance;
	}

	public static interface App42UserServiceListener {
		public void onUserCreated(User response);

		public void onCreationFailed(App42Exception exception);

		public void onGetUserSuccess(User response);

		public void onGetUserFailed(App42Exception exception);

		public void onUserAuthenticated(User response);

		public void onAuthenticationFailed(App42Exception exception);

	}

	/*
	 * This function Stores JSON Document.
	 */
	public void insertJSONDoc(final String dbName, final String collectionName,
			final JSONObject json, final App42StorageServiceListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final Storage response = storageService.insertJSONDocument(dbName, collectionName, json);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onDocumentInserted(response);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onInsertionFailed(ex);
							}
						}
					});
				}
			}
		}.start();
	}
	
	/*
	 * This function Find JSON Document By Id.
	 */
	public void findDocByColletion(final String dbName, final String collectionName,
			final int type, final App42StorageServiceListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final Storage response = storageService.findAllDocuments(dbName, collectionName);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onFindDocSuccess(response, type);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onFindDocFailed(ex);
							}
							ex.printStackTrace();
						}
					});
				}
			}
		}.start();
	}
	
	/*
	 * This function Find JSON Document By Id.
	 */
	public void updateDocByKeyValue(final String dbName,
			final String collectionName, final String key, final String value,
			final JSONObject newJsonDoc, final App42StorageServiceListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final Storage response = storageService.updateDocumentByKeyValue(dbName, collectionName, key, value, newJsonDoc);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onUpdateDocSuccess(response);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onUpdateDocFailed(ex);
							}
						}
					});
				}
			}
		}.start();
	}

	public static interface App42StorageServiceListener 
	{
		
		public void onDocumentInserted(Storage response);
	
		public void onUpdateDocSuccess(Storage response);

		public void onFindDocSuccess(Storage response, int type);

		public void onInsertionFailed(App42Exception ex);
		
		public void onFindDocFailed(App42Exception ex);

		public void onUpdateDocFailed(App42Exception ex);
	}

	
	public static interface App42ScoreBoardServiceListener 
	{
		void onSaveScoreSuccess(Game response);

		void onSaveScoreFailed(App42Exception ex);
		
		void onLeaderBoardSuccess(Game response);
        
		void onLeaderBoardFailed(App42Exception ex);
	}
	
	/*
	 * This function Uploads File On App42 Cloud.
	 */
	public void uploadImage(final String name,
			final String filePath, final UploadFileType fileType, final String description, final App42UploadServiceListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final Upload response = uploadService.uploadFile(name, filePath, UploadFileType.IMAGE, description);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onUploadImageSuccess(response);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onUploadImageFailed(ex);
							}
						}
					});
				}
			}
		}.start();
	}
	
	
	/*
	 * This function Uploads File On App42 Cloud.
	 */
	public void getImage(final String fileName, final App42UploadServiceListener callBack) {
		final Handler callerThreadHandler = new Handler();
		new Thread() {
			@Override
			public void run() {
				try {
					final Upload response = uploadService.getFileByName(fileName);
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							callBack.onGetImageSuccess(response);
						}
					});
				} catch (final App42Exception ex) {
					callerThreadHandler.post(new Runnable() {
						@Override
						public void run() {
							if (callBack != null) {
								callBack.onGetImageFailed(ex);
							}
						}
					});
				}
			}
		}.start();
	}
	
	
	public static interface App42UploadServiceListener 
	{
		void onUploadImageSuccess(Upload response);

		void onUploadImageFailed(App42Exception ex);

		void onGetImageSuccess(Upload response);

		void onGetImageFailed(App42Exception ex);
	}

}