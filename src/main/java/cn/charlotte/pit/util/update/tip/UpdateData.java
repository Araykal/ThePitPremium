package cn.charlotte.pit.util.update.tip;

import cn.charlotte.pit.util.chat.CC;
import org.bukkit.entity.Player;
import xyz.upperlevel.spigot.book.BookUtil;

/**
 * @Author: Misoryan
 * @Created_In: 2021/2/2 13:59
 */
public class UpdateData {

    public static void openUpdateMessageBook(Player player) {
        BookUtil.openPlayer(player,
                BookUtil.writtenBook()
                        .title(CC.translate("&e新版本更新日志"))
                        .author(player.getName())
                        .pages(
                                new BookUtil.PageBuilder()
                                        .add(
                                                BookUtil.TextBuilder
                                                        .of(CC.translate("&0一个新更新在最近发布!"))
                                                        .build()
                                        )
                                        .newLine()
                                        .newLine()
                                        .add(
                                                CC.translate("&0详细的更新内容,请查阅:")
                                        )
                                        .newLine()
                                        .add(
                                                BookUtil.TextBuilder
                                                        .of(CC.translate("&a&l天坑乱斗更新日志"))
                                                        .onHover(BookUtil.HoverAction.showText(CC.translate("&f点击查看")))
                                                        .onClick(BookUtil.ClickAction.openUrl("https://shimo.im/docs/qRcVP3yW8XQKYgjc"))
                                                        .build()
                                        )
                                        .build()
                        )
                        .build()
        );
    }
}
