package virgil.dailycost.listeners;

import virgil.dailycost.ROOM.Category_Record;

public interface CategoryItemListener {
    void categoryItemRenameListener(Category_Record record);
    void categoryItemRemoveListener(Category_Record record);
}
