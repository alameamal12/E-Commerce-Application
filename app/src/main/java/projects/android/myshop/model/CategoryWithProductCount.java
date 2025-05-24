package projects.android.myshop.model;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

import projects.android.myshop.db.entity.CategoryEntity;

public class CategoryWithProductCount {
    @Embedded
    public CategoryEntity category;

    @ColumnInfo(name = "productCount")
    public long productCount;
}
