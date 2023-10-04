import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rival.githubusersapp.Utils.UserDiffCallback
import com.rival.githubusersapp.data.model.GithubItemUser

import com.rival.githubusersapp.databinding.ItemRowUsersBinding

class UserAdapter(private val listUser: List<GithubItemUser>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback


    interface OnItemClickCallback {
        fun onItemClicked(data: GithubItemUser)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(var binding: ItemRowUsersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding =
            ItemRowUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int = listUser.size



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataUser = listUser[position]

        holder.binding.tvUsers.text = dataUser.login

        Glide.with(holder.itemView.context)
            .load(dataUser.avatarUrl)
            .into(holder.binding.ivUsers)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[position])
        }

    }

}