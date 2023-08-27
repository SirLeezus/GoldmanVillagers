package lee.code.villagers.database.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "villagers")
public class VillagerTable {
  @DatabaseField(id = true, canBeNull = false)
  private int id;

  @DatabaseField(columnName = "location", canBeNull = false)
  private String location;

  @DatabaseField(columnName = "name", canBeNull = false)
  private String name;

  @DatabaseField(columnName = "type", canBeNull = false)
  private String type;

  @DatabaseField(columnName = "profession", canBeNull = false)
  private String profession;
  @DatabaseField(columnName = "command_type")
  private String commandType;

  @DatabaseField(columnName = "command")
  private String command;

  @DatabaseField(columnName = "level")
  private int level;

  public VillagerTable(int id, String location, String name, String type, String profession) {
    this.id = id;
    this.location = location;
    this.name = name;
    this.type = type;
    this.profession = profession;
  }
}
