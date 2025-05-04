package cc.meltryllis.nf.entity.property;

import cc.meltryllis.nf.constants.DataCons;
import cc.meltryllis.nf.entity.property.output.ReplacementProperty;
import cc.meltryllis.nf.utils.jackson.JSONUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户输入的历史记录。
 * 主要用于程序的再次读取，方便用户再次选择。
 *
 * @author Zachary W
 * @date 2025/3/25
 */
@Getter
public class HistoryProperty {

    public static HistoryProperty instance;

    private final UniqueObservableList<File>                inputFileHistoryList;
    private final UniqueObservableList<ReplacementProperty> outputReplacementHistoryList;

    @JsonCreator
    public HistoryProperty(@JsonProperty("inputFileHistoryList") List<File> inputFileHistoryList,
                           @JsonProperty("outputReplacementHistoryList") List<ReplacementProperty> outputReplacementHistoryList) {
        if (instance != null) {
            throw new RuntimeException();
        }
        this.inputFileHistoryList = new UniqueObservableList<>(inputFileHistoryList);
        this.inputFileHistoryList.setDuplicatePolicy(UniqueObservableList.DuplicatePolicy.SET_TO_FIRST);
        this.outputReplacementHistoryList = new UniqueObservableList<>(outputReplacementHistoryList);
        this.outputReplacementHistoryList.setDuplicatePolicy(UniqueObservableList.DuplicatePolicy.SET_TO_FIRST);
    }

    public static HistoryProperty getInstance() {
        if (instance == null) {
            HistoryProperty property = JSONUtil.parseObject(new File(DataCons.HISTORY), HistoryProperty.class);
            instance = property == null ? new HistoryProperty(new ArrayList<>(), new ArrayList<>()) : property;
        }
        return instance;
    }

    public static void store() {
        JSONUtil.storeFile(new File(DataCons.HISTORY), getInstance());
    }

}
