package com.anpa.anpacr.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.GenericNameValue;
import com.anpa.anpacr.domain.Lost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DetailLostActivity extends AnpaAppFraqmentActivity {
	private Animator mCurrentAnimator;
	private int mShortAnimationDuration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_lost);
		
		//Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_LOST);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Lost value = (Lost) extras.get(Constants.ID_OBJ_DETAIL_LOST);

			TextView txt_nom_mascota = (TextView) findViewById(R.id.txt_nom_mascota);
			txt_nom_mascota.setText(value.get_snombreMascota());
			
			TextView txt_raza = (TextView) findViewById(R.id.txt_raza_mascota);
			String raza = readSpecies(value.get_sespecie(), value.get_sraza());
			txt_raza.setText(raza);
			
			TextView txt_contacto = (TextView) findViewById(R.id.txt_contacto);
			txt_contacto.setText(value.get_snombreDueno());
			
			TextView txt_telefono = (TextView) findViewById(R.id.txt_telefono);
			txt_telefono.setText(value.get_stelefono());
			
			TextView txt_short_direction = (TextView) findViewById(R.id.txt_short_direction);

            String provincia = "";

            for (String prov : Constants.PROVINCE) {
                String[] provSplit = prov.split(",");
                if(provSplit[0].contains(value.get_iprovinvia().toString())){
                    provincia = provSplit[1];
                    break;
                }
            }
			

			String canton = readCantones(value.get_iprovinvia(), value.get_icanton());


            String txtShortDirection = canton + ", " + provincia;
			txt_short_direction.setText(txtShortDirection);

			TextView txt_detail_lost_description = (TextView) findViewById(R.id.txt_detail_lost_description);
			txt_detail_lost_description.setText(value.get_sdetalle());

			TextView txt_detail_lost_date = (TextView) findViewById(R.id.txt_detail_lost_date);
			txt_detail_lost_date.setText(value.get_sDate());

			if (value.get_bFoto() != null) {
				final ImageView img_detail_lost = (ImageView) findViewById(R.id.img_detail_lost);
				final Bitmap bmp = BitmapFactory.decodeByteArray(value.get_bFoto(), 0, value.get_bFoto().length);
				img_detail_lost.setImageBitmap(bmp);

				img_detail_lost.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						zoomImageFromThumb(img_detail_lost, bmp);
					}
				});
				mShortAnimationDuration = getResources().getInteger(
						android.R.integer.config_shortAnimTime);
			}
		}
		
		Button btnAddLost = (Button)findViewById(R.id.btn_add_lost);
		btnAddLost.setOnClickListener(onSearch);
	}


	/* carga la lista de razas de una especie */
	private String readSpecies(int specieId, int raza)
	{
		ArrayList<GenericNameValue> speciesList = new ArrayList<GenericNameValue>();

		String selectedFile = "";
		switch (specieId) {
			case 2:
				selectedFile = "razas_gatos";
				break;
			case 3:
				selectedFile = "razas_aves";
				break;
			case 4:
				selectedFile = "razas_peces";
				break;
			case 5:
				selectedFile = "razas_roedores";
				break;
			default:
				selectedFile = "razas_perros";
				break;
		}

		BufferedReader in = null;
		StringBuilder buf = new StringBuilder();
		try{
			InputStream is = getApplicationContext().getAssets().open(selectedFile + ".txt");
			in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String races;
			boolean isFirst = true;
			while ((races = in.readLine()) != null ){
				if (isFirst)
					isFirst = false;
				else
					buf.append('\n');
				buf.append(races);
			}

			String[] specieRacesArray = buf.toString().split("#");

			for (String race : specieRacesArray)
			{
				String[] values = race.split(",");
				if(raza == Integer.parseInt(values[0]))
					return values[1];
			}
		}
		catch(IOException e) {
			Log.e("OJO", "Error opening asset ");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					Log.e("OJO", "Error closing asset ");
				}
			}
		}
		return "";
	}


    /* carga la lista de cantones de una especie */
    private String readCantones(int provinciaId, int cantonId)
    {
        ArrayList<GenericNameValue> cantonesList = new ArrayList<GenericNameValue>();

        String selectedFile = "";
        switch (provinciaId) {
            case 2:
                selectedFile = "canton_alajuela";
                break;
            case 3:
                selectedFile = "canton_cartago";
                break;
            case 4:
                selectedFile = "canton_heredia";
                break;
            case 5:
                selectedFile = "canton_guanacaste";
                break;
            case 6:
                selectedFile = "canton_puntarenas";
                break;
            case 7:
                selectedFile = "canton_limon";
                break;
            default:
                selectedFile = "canton_san_jose";
                break;
        }

        BufferedReader in = null;
        StringBuilder buf = new StringBuilder();
        try{
            InputStream is = getApplicationContext().getAssets().open(selectedFile + ".txt");
            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

            String cantones;
            boolean isFirst = true;
            while ((cantones = in.readLine()) != null ){
                if (isFirst)
                    isFirst = false;
                else
                    buf.append('\n');
                buf.append(cantones);
            }

            String[] cantonArray = buf.toString().split("#");

            for (String canton : cantonArray)
            {
                String[] values = canton.split(",");
                if(cantonId == Integer.parseInt(values[0]))
                    return values[1];
            }
        }
        catch(IOException e) {
            Log.e("OJO", "Error opening asset ");
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    Log.e("OJO", "Error closing asset ");
                }
            }
        }
        return "";
    }
	
	/**
	 * Listener del bot�n
	 */
	private OnClickListener onSearch = new OnClickListener() {
		@Override
		public void onClick(View v) {
			startActivity(new Intent(DetailLostActivity.this, AddLostActivity.class));
		}
	};

	//Zoom de la imagen
	private void zoomImageFromThumb(final View thumbView, Bitmap imageResId) {
		// If there's an animation in progress, cancel it
		// immediately and proceed with this one.
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		// Load the high-resolution "zoomed-in" image.
		/*final ImageView expandedImageView = new ImageView(DetailNewsActivity.this);
		expandedImageView.setImageBitmap(imageResId);*/
		final ImageView expandedImageView = (ImageView) findViewById(
				R.id.expanded_image);
		expandedImageView.setImageBitmap(imageResId);

		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step involves lots of math. Yay, math.
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the final bounds are the global visible rectangle of the container
		// view. Also set the container view's offset as the origin for the
		// bounds, since that's the origin for the positioning animation
		// properties (X, Y).
		thumbView.getGlobalVisibleRect(startBounds);
		findViewById(R.id.ll_detail_content_lost)
				.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the "center crop" technique. This prevents undesirable
		// stretching during the animation. Also calculate the start scaling
		// factor (the end scaling factor is always 1.0).
		float startScale;
		if ((float) finalBounds.width() / finalBounds.height()
				> (float) startBounds.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}

		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins, it will position the zoomed-in view in the place of the
		// thumbnail.
		thumbView.setAlpha(0f);
		expandedImageView.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations
		// to the top-left corner of the zoomed-in view (the default
		// is the center of the view).
		expandedImageView.setPivotX(0f);
		expandedImageView.setPivotY(0f);

		// Construct and run the parallel animation of the four translation and
		// scale properties (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set
				.play(ObjectAnimator.ofFloat(expandedImageView, View.X,
						startBounds.left, finalBounds.left))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
						startBounds.top, finalBounds.top))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
						startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
				View.SCALE_Y, startScale, 1f));
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down
		// to the original bounds and show the thumbnail instead of
		// the expanded image.
		final float startScaleFinal = startScale;
		expandedImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCurrentAnimator != null) {
					mCurrentAnimator.cancel();
				}

				// Animate the four positioning/sizing properties in parallel,
				// back to their original values.
				AnimatorSet set = new AnimatorSet();
				set.play(ObjectAnimator
						.ofFloat(expandedImageView, View.X, startBounds.left))
						.with(ObjectAnimator
								.ofFloat(expandedImageView,
										View.Y,startBounds.top))
						.with(ObjectAnimator
								.ofFloat(expandedImageView,
										View.SCALE_X, startScaleFinal))
						.with(ObjectAnimator
								.ofFloat(expandedImageView,
										View.SCALE_Y, startScaleFinal));
				set.setDuration(mShortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}
				});
				set.start();
				mCurrentAnimator = set;
			}
		});
	}
}
