package com.example.cinema

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class TicketAdapter(
    private val tickets: List<TicketResponse>,
    private val onTicketClick: (TicketResponse) -> Unit
) : RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    class TicketViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvMovieName: TextView =
            itemView.findViewById(R.id.tvItemMovieName)

        val tvBookingCode: TextView =
            itemView.findViewById(R.id.tvItemBookingCode)

        val tvCinema: TextView =
            itemView.findViewById(R.id.tvItemCinema)

        val tvTime: TextView =
            itemView.findViewById(R.id.tvItemTime)

        val tvSeats: TextView =
            itemView.findViewById(R.id.tvItemSeats)

        val tvStatus: TextView =
            itemView.findViewById(R.id.tvItemStatus)

        val tvPrice: TextView =
            itemView.findViewById(R.id.tvItemPrice)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TicketViewHolder {

        val view =
            LayoutInflater
                .from(parent.context)
                .inflate(
                    R.layout.item_ticket,
                    parent,
                    false
                )

        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TicketViewHolder,
        position: Int
    ) {

        val ticket =
            tickets[position]

        holder.tvMovieName.text =
            ticket.movieName

        holder.tvBookingCode.text =
            "Mã vé: ${ticket.bookingCode}"

        holder.tvCinema.text =
            "${ticket.cinemaName} • ${ticket.roomName}"

        holder.tvTime.text =
            "${ticket.date} • ${ticket.showtime}"

        holder.tvSeats.text =
            "Ghế: ${ticket.seats}"

        holder.tvStatus.text =
            formatStatus(
                ticket.bookingStatus
            )

        val formatter =
            NumberFormat.getNumberInstance(
                Locale("vi", "VN")
            )

        holder.tvPrice.text =
            "${formatter.format(ticket.totalAmount)}đ"

        holder.itemView.setOnClickListener {
            onTicketClick(ticket)
        }
    }

    override fun getItemCount(): Int =
        tickets.size

    private fun formatStatus(
        status: String
    ): String {

        return when (
            status.uppercase()
        ) {

            "CONFIRMED" ->
                "Đã xác nhận"

            "PENDING" ->
                "Chờ xác nhận"

            "CANCELLED" ->
                "Đã hủy"

            else ->
                status
        }
    }
}