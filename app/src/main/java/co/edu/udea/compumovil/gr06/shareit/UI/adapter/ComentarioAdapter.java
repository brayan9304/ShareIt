package co.edu.udea.compumovil.gr06.shareit.UI.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import co.edu.udea.compumovil.gr06.shareit.R;
import co.edu.udea.compumovil.gr06.shareit.UI.model.CommentUser;

/**
 * Created by jaime on 18/11/2016.
 */

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.comentarioHolder> {
    List<CommentUser> comentarios;

    public ComentarioAdapter(@NonNull List<CommentUser> data) {
        this.comentarios = data;
    }

    @Override
    public comentarioHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comentarios_card, parent, false);
        return new ComentarioAdapter.comentarioHolder(view);
    }

    @Override
    public void onBindViewHolder(comentarioHolder holder, int position) {

        CommentUser comentario = comentarios.get(position);
        holder.getValoracion().setRating((float) comentario.getScore());
        holder.getComentario().setText(comentario.getComentario());
        holder.getAutor().setText(comentario.getAutor());
        holder.getFecha().setText(comentario.getFecha());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void rellenarAdapter(CommentUser datos) {
        comentarios.add(datos);
        notifyItemInserted(getItemCount() - 1);
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    class comentarioHolder extends RecyclerView.ViewHolder {
        private RatingBar valoracion;
        private TextView comentario;
        private TextView autor;
        private TextView fecha;

        public comentarioHolder(View itemView) {
            super(itemView);
            valoracion = (RatingBar) itemView.findViewById(R.id.valoracion);
            comentario = (TextView) itemView.findViewById(R.id.comentario_valoracion);
            autor = (TextView) itemView.findViewById(R.id.usuario_valoracion);
            fecha = (TextView) itemView.findViewById(R.id.fecha_valoracion);
        }

        public RatingBar getValoracion() {
            return valoracion;
        }

        public TextView getComentario() {
            return comentario;
        }

        public TextView getAutor() {
            return autor;
        }

        public TextView getFecha() {
            return fecha;
        }
    }
}
