package com.anpa.anpacr.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.Lost;

public class DetailLostActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_lost);
		
		//Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_LOST);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Lost value = (Lost) extras.get(Constants.ID_OBJ_DETAIL_LOST);

			TextView txt_nom_mascota = (TextView) findViewById(R.id.txt_nom_mascota);
			txt_nom_mascota.setText(value.get_snombreMascota());
			
			TextView txt_raza = (TextView) findViewById(R.id.txt_raza_mascota);
			txt_raza.setText(value.get_sraza());
			
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
					
			String canton = "";
			
			for (String cant : Constants.CANTON) {
				String[] cantSplit = cant.split(",");
				if(cantSplit[1].contains(value.get_icanton().toString()))
					 canton = cantSplit[2];
			}	
			
			
			String txtShortDirection = canton + ", " + provincia;
			txt_short_direction.setText(txtShortDirection);

			TextView txt_detail_lost_description = (TextView) findViewById(R.id.txt_detail_lost_description);
			txt_detail_lost_description.setText(value.get_sdetalle());

			TextView txt_detail_lost_date = (TextView) findViewById(R.id.txt_detail_lost_date);
			txt_detail_lost_date.setText(value.get_sDate());

			if (value.get_bFoto() != null) {
				ImageView img_detail_lost = (ImageView) findViewById(R.id.img_detail_lost);
				Bitmap bmp = BitmapFactory.decodeByteArray(value.get_bFoto(), 0, value.get_bFoto().length);
				img_detail_lost.setImageBitmap(bmp);
			}
		}
		
		Button btnAddLost = (Button)findViewById(R.id.btn_add_lost);
		btnAddLost.setOnClickListener(onSearch);
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

}
