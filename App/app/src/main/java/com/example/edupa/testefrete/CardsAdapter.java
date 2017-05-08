package com.example.edupa.testefrete;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by edupa on 20/02/2017.
 */

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder>{
    List<DadosCards> list;
    Main2Activity context;
    private String STATUS = "Status: ";
    private String MOTORISTA = "Motorista: ";
    private String CARGA = "Carga: ";
    private String PESO = "Peso: ";
    private String UNIDADE_PESO = "kg";
    public CardsAdapter(Main2Activity context, List<DadosCards> list){
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_layout, parent, false);
        ViewHolder pvh = new ViewHolder(v, context);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.status.setText(list.get(position).getStatus());
        holder.nome.setText(MOTORISTA+list.get(position).getNomeMot());
        holder.carga.setText(CARGA+list.get(position).getCarga());
        holder.peso.setText(PESO+list.get(position).getPeso()+UNIDADE_PESO);
        holder.cI.setText(list.get(position).getCidadeI());
        holder.cF.setText(list.get(position).getCidadeF());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView status, nome, carga, peso, cI, cF;

        public ViewHolder(View itemView, Main2Activity context) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            status = (TextView)itemView.findViewById(R.id.info_text);
            nome = (TextView)itemView.findViewById(R.id.textView2);
            carga = (TextView)itemView.findViewById(R.id.textView5);
            peso = (TextView)itemView.findViewById(R.id.textView6);
            cI = (TextView)itemView.findViewById(R.id.textView3);
            cF = (TextView)itemView.findViewById(R.id.textView4);
            View view1 = (View) itemView.findViewById(R.id.circle1);
            View view2 = (View) itemView.findViewById(R.id.circle2);
            View view3 = (View) itemView.findViewById(R.id.circle3);
            ImageView img1 = (ImageView)itemView.findViewById(R.id.imageView3);
            int tC = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5,
                    context.getResources().getDisplayMetrics());//Tamanho em DP dos circulos
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tC, tC);
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            int mT = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                    context.getResources().getDisplayMetrics());//Margin Top
            params.setMargins(width/4, mT, 0, 0);
            view1.setLayoutParams(params);
            RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(tC, tC);
            params2.setMargins((width*3)/8, mT, 0, 0);
            view2.setLayoutParams(params2);
            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams)view3.getLayoutParams();
            params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params3.rightMargin = (width*3)/8;
            view3.setLayoutParams(params3);
            RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams)img1.getLayoutParams();
            params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params4.rightMargin = (width)/4;
            img1.setLayoutParams(params4);
        }
    }
}