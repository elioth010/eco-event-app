package com.bytesw.consultadecuentas.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bytesw.consultadecuentas.R;

public class NavigationItemsAdapter extends	RecyclerView.Adapter<NavigationItemsAdapter.ViewHolder> {

	// verifica si se esta llenando el encabezado o el contenido del la lista
	private static final int TYPE_HEADER = 0; 
	private static final int TYPE_ITEM = 1;
	// titutlos de menus disponibles tomados desde el activity como arg del constructor
	private String mNavTitles[]; 
	// private int mIcons[]; //lista de iconos a pintar en el drawer

	private String name; // nombre del cliente logueado
	// int Resource para imagen del cliente se debe recibir como parametro del ws como una url
	private int profile = R.drawable.ic_profile; 
	private String secondaryText; // email empresa alguna otra descripcion

	// se crea una ViewHolder que exienda de RecyclerView.ViewHolder
	// ViewHolder se usa para almacenar las vistas infladas(inflated) en orden
	// para reciclarlas

	public static class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

		private int holderId;

		private TextView textView;
		private ImageView imageView;
		private ImageView profile;
		private TextView Name;
		private TextView secondaryText;
		private Context context;
		private ViewGroup parent;

		public ViewHolder(View itemView, int ViewType, Context context, ViewGroup parent) {
			super(itemView);
			this.context = context;
			itemView.setClickable(true);
			itemView.setOnClickListener(this);
			this.parent = parent;
			// Se crea la vista dependiendo el parametro que se recibe si es el
			// encabezado o solo el cuerpo
			if (ViewType == TYPE_ITEM) {
				// estos estan declarados en item_navigation_drawer.xml
				textView = (TextView) itemView.findViewById(R.id.item_text_navigation_drawer);
				imageView = (ImageView) itemView.findViewById(R.id.item_icon_navigation_drawer);
				holderId = 1; // se pone holder id que ya lleno los items
			} else {
				// estan declarados en header_navigation_drawer.xml
				Name = (TextView) itemView.findViewById(R.id.header_profile_name);
				secondaryText = (TextView) itemView.findViewById(R.id.header_secondary_text);
				// foto del recurso
				profile = (ImageView) itemView.findViewById(R.id.item_icon_navigation_drawer); 
				holderId = 0; // se pone a 0 indica que ya lleno el encabezado
			}
			setDefaultSelection(parent);
		}

		public void setDefaultSelection(ViewGroup parent) {
			if(Build.VERSION.SDK_INT <21){
				if (parent.getChildCount() > 1) {
					parent.getChildAt(1).setBackgroundResource(R.color.gray_light);
					Drawable backgrounds[] = new Drawable[2];
			        Resources res = context.getResources();
			        backgrounds[0] = res.getDrawable(R.drawable.custom_list_view_shape_pressed);
			        backgrounds[1] = res.getDrawable(R.drawable.custom_button_shape_pressed);
					TransitionDrawable transition = new TransitionDrawable(backgrounds);
					parent.getChildAt(1).setBackground(transition);
					transition.startTransition(600);
					LinearLayout layout = (LinearLayout) parent.getChildAt(1);
					((TextView)layout.getChildAt(1)).setTextColor(context.getResources().getColor(R.color.icons));
				}
			}else{
				if (parent.getChildCount() > 1) {
					parent.getChildAt(1).setSelected(true);
					//parent.getChildAt(1).setBackgroundResource(R.color.gray_light);
					Drawable backgrounds[] = new Drawable[2];
			        Resources res = context.getResources();
			        backgrounds[0] = res.getDrawable(R.drawable.list_view_ripple);
			        backgrounds[1] = res.getDrawable(R.drawable.custom_button_shape_pressed);
					TransitionDrawable transition = new TransitionDrawable(backgrounds);
					parent.getChildAt(1).setBackground(transition);
					transition.startTransition(600);
					LinearLayout layout = (LinearLayout) parent.getChildAt(1);
					((TextView)layout.getChildAt(1)).setTextColor(context.getResources().getColor(R.color.icons));
				}
				
			}
			
			
		}

		public void clearSelection(ViewGroup parent) {
			for (int i = 1; i < parent.getChildCount(); i++) {
				View v = parent.getChildAt(i);
				if (Build.VERSION.SDK_INT < 21) {
					v.setBackground(context.getResources().getDrawable(R.drawable.custom_list_view));
					LinearLayout layout = (LinearLayout) v;
					((TextView)layout.getChildAt(1)).setTextColor(context.getResources().getColor(R.color.primary_text));
					
				}else{
					v.setSelected(false);
					v.setBackground(context.getResources().getDrawable(R.drawable.list_view_ripple));
					LinearLayout layout = (LinearLayout) v;
					((TextView)layout.getChildAt(1)).setTextColor(context.getResources().getColor(R.color.primary_text));
				}
				
			}
		}

		@Override
		public void onClick(View v) {
			System.out.println(parent);
			clearSelection(parent);
			Toast.makeText(context, "The position :" + getPosition(), Toast.LENGTH_SHORT).show();
			
			if(getPosition()>0){
				if(Build.VERSION.SDK_INT < 21){
					//v.setBackgroundResource(R.color.gray_light);
					Drawable backgrounds[] = new Drawable[2];
			        Resources res = context.getResources();
			        backgrounds[0] = res.getDrawable(R.drawable.custom_list_view_shape_pressed);
			        backgrounds[1] = res.getDrawable(R.drawable.custom_button_shape_pressed);
			        TransitionDrawable transition = new TransitionDrawable(backgrounds);
					v.setBackground(transition);
					transition.startTransition(600);
					LinearLayout layout = (LinearLayout) v;
					((TextView)layout.getChildAt(1)).setTextColor(context.getResources().getColor(R.color.icons));
				}else{
					v.setSelected(true);
					Drawable backgrounds[] = new Drawable[2];
			        Resources res = context.getResources();
			        backgrounds[0] = res.getDrawable(R.drawable.list_view_ripple);
			        backgrounds[1] = res.getDrawable(R.drawable.custom_button_shape_pressed);
					TransitionDrawable transition = new TransitionDrawable(backgrounds);
					v.setBackground(transition);
					transition.startTransition(600);
					LinearLayout layout = (LinearLayout) v;
					((TextView)layout.getChildAt(1)).setTextColor(context.getResources().getColor(R.color.icons));
					//v.setBackgroundResource(R.color.gray_light);
				}
			}
		}

	}

	public NavigationItemsAdapter(String titles[], String name, String secondaryText) {
		// titles, icons, name, secondary text, profile pic vienen desde el
		// activity
		mNavTitles = titles; // have seen earlier
		// mIcons = Icons; fijado estatico los menus del nav
		this.name = name;
		this.secondaryText = secondaryText;
	}

	// sobre escribimos el metodo onCreateViewHolder que es llamado
	// cuando el ViewHolder es creado.
	// se infla el item_navigation_drawer.xml y si
	// el viewType es TYPE_ITEM se infla los items
	// pero si el viewType is TYPE_HEADER
	// se infla el encabezado

	@Override
	public NavigationItemsAdapter.ViewHolder onCreateViewHolder(
			ViewGroup parent, int viewType) {

		if (viewType == TYPE_ITEM) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_navigation_drawer, parent, false);
			ViewHolder vhItem = new ViewHolder(v, viewType,	parent.getContext(), parent);
			return vhItem;
		} else if (viewType == TYPE_HEADER) {
			View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_navigation_drawer, parent, false);
			ViewHolder vhHeader = new ViewHolder(v, viewType, parent.getContext(), parent);
			return vhHeader;
		}
		return null;

	}

	// se sobre carga el metodo que es invocado cuando el item en la fila se
	// necista mostrar
	// y en que posicion de manera recursiva.
	@Override
	public void onBindViewHolder(NavigationItemsAdapter.ViewHolder holder, int position) {

		if (holder.holderId == 1) {
			// si se trata de los items es uno
			// le colocamos la lista de items que se disponen
			holder.textView.setText(mNavTitles[position - 1]);
			// se deveria poner el array de iconos de cada menu pero de momento
			// es estatico
			holder.imageView.setImageResource(android.R.drawable.ic_menu_send);
		} else {
			// ahora se el encabezado por lo tanto se coloca la imagen del
			// recurso
			// en este caso el cliente
			holder.profile.setImageResource(profile);
			// se coloca el nombre el recurso
			holder.Name.setText(name);
			// e informacion adicional que se desea agregar
			holder.secondaryText.setText(secondaryText);
		}
	}

	/**
	 * @return Numero de filas del nav
	 */
	@Override
	public int getItemCount() {
		return mNavTitles.length + 1;
	}

	/**
	 * @return retorna el tipo de vista, <br>
	 *         <h4>TYPE_HEADER = 0</h4> <br>
	 *         <h4>TYPE_ITEM = 1</h4>
	 */
	@Override
	public int getItemViewType(int position) {
		if (isPositionHeader(position)) {
			return TYPE_HEADER;
		} else {
			return TYPE_ITEM;
		}
	}

	/**
	 * 
	 * @param position
	 * @return true si la posicion es el encabezado
	 */
	private boolean isPositionHeader(int position) {
		return position == 0;
	}

}