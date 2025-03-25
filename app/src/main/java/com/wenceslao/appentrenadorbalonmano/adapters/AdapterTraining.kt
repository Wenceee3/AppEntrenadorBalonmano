import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wenceslao.appentrenadorbalonmano.R
import com.wenceslao.appentrenadorbalonmano.models.Training
import com.wenceslao.appentrenadorbalonmano.viewholder.ViewHTraining

class AdapterTraining(
    private var trainingList: MutableList<Training>,
    private val onDeleteItem: (Int) -> Unit,
    private val onEditItem: (Int) -> Unit
) : RecyclerView.Adapter<ViewHTraining>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHTraining {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_training, parent, false)
        return ViewHTraining(view)
    }

    override fun onBindViewHolder(holder: ViewHTraining, position: Int) {
        holder.renderize(
            trainingList[position],
            position,
            onDelete = { pos -> onDeleteItem(pos) },
            onEdit = { pos -> onEditItem(pos) }
        )
    }

    override fun getItemCount(): Int = trainingList.size
}
