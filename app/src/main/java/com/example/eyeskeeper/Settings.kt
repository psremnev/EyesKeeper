package com.example.eyeskeeper

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.CheckBox
import androidx.appcompat.widget.SwitchCompat

open class Settings : DialogFragment() {
    private var settings: Constants.SettingsData? = null
    private var dataHelper: DataHelper? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        dataHelper = DataHelper(requireContext().applicationContext)
        settings = dataHelper?.getSettings()
        initPeriodSelector()
        initPeriodTimeSelector()
        initCharacterSelector()
        initVibrateBtn()

        val closeBtn: Button? = view?.findViewById(R.id.closeSettings)
        closeBtn?.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }

    }

    /** Инициализируем выбор периода уведомлений
     */
    private fun initPeriodSelector() {
        val periodField: EditText? = view?.findViewById(R.id.periodMin)
        settings?.period?.let { periodField?.setText(it.toString()) }
        periodField?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length !== 0) {
                    dataHelper?.saveSettings(s.toString().toInt(), null, null, null)
                }
            }
        })
    }

    /** Инициализируем выбор времени периода
     */
    private fun initPeriodTimeSelector() {
        val periodTimeField: EditText? = view?.findViewById(R.id.periodTimeMin)
        settings?.periodTime?.let { periodTimeField?.setText(it.toString()) }
        periodTimeField?.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().length !== 0) {
                    dataHelper?.saveSettings(null, s.toString().toInt(), null, null)
                }
            }
        })
    }

    /** Инициализируем выбор персонажа
     */
    private fun initCharacterSelector() {
        val data: ArrayList<Constants.Character> = arrayListOf(
            object: Constants.Character {
                override var isChecked = false
                override val checkBoxText: String = Constants.CHARACTER_TYPE.CLASSIC.value
                override val characterType = Constants.CHARACTER_IMAGE_ID.CLASSIC.value
            },
            object: Constants.Character {
                override var isChecked = false
                override val checkBoxText: String = Constants.CHARACTER_TYPE.CAT.value
                override val characterType = Constants.CHARACTER_IMAGE_ID.CAT.value
            })
        val characterPos: Int? = settings?.character
        if (characterPos is Int && characterPos <= data.size - 1) {
            data[settings?.character!!].isChecked = true
        } else {
            data[Constants.CHARACTER_TYPE_MAP[Constants.CHARACTER_TYPE.CLASSIC.value]!!].isChecked = true
        }

        val characterList: RecyclerView? = view?.findViewById(R.id.characterList)
        characterList?.layoutManager = LinearLayoutManager(requireContext().applicationContext, LinearLayoutManager.HORIZONTAL, false)
        characterList?.adapter = CharacterListAdapter(requireContext(), dataHelper, data)
    }

    private fun initVibrateBtn() {
        val vibrateBtn: SwitchCompat? = view?.findViewById(R.id.vibrateBtn)
        vibrateBtn?.isChecked = settings?.vibrate == true
        vibrateBtn?.setOnClickListener {
            val isChecked: Boolean = vibrateBtn?.isChecked as Boolean
            dataHelper?.saveSettings(null, null, null, isChecked)
        }
    }
}

/** Адаптер для списка выбора персонажа
 * @param characterList {ArrayList<Constants.Character>} - массив персонажей
 */
class CharacterListAdapter(private val ctx: Context,
                           private val dataHelper: DataHelper?,
                           private val characterList: ArrayList<Constants.Character>) : RecyclerView.Adapter<CharacterListAdapter.ViewHolder>() {

    var lastChecked: CheckBox? = null

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBox: CheckBox? = null
        var image: ImageView? = null

        init {
            checkBox = itemView.findViewById(R.id.character_check)
            image = itemView.findViewById(R.id.character_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.character_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val checkBox: CheckBox? = holder.checkBox
        val isChecked: Boolean = characterList[position].isChecked

        // инициализируем последний выбранный чек бокс
        if (isChecked) {
            lastChecked = checkBox
        }
        holder.checkBox?.isChecked = isChecked
        holder.checkBox?.text = characterList[position].checkBoxText
        holder.image?.setImageResource(characterList[position].characterType)
        holder.checkBox!!.setOnClickListener { view ->
            val checkBox = view as CheckBox
            dataHelper?.saveSettings(null, null, Constants.CHARACTER_TYPE_MAP[checkBox.text], null)
            lastCheckedInit(checkBox)
        }
    }

    override fun getItemCount(): Int {
        return characterList!!.size
    }

    /** Снимает последний чекбокс и устанавливает новый последний
     * @param newChecked {CheckBox} - новый выбранный чекбокс
     */
    private fun lastCheckedInit(newChecked: CheckBox) {
        lastChecked?.isChecked = false
        lastChecked = newChecked
    }
}