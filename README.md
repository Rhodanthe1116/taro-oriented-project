# taro-oriented-project
## log
###### tags: `程式專案`,`紫芋導向`,`start travel`
### alpha-1.6
> 版本資訊：
> -
> 
> **概述**
> > 新增彩蛋~~~~~~~~
> > 移植地區選擇至搜尋欄並完整化
> > 實作訂購、取消訂單
> 
> **類別更動**
> - 新增PlaceSuggestion
> - 新增PlaceCounselor
> - 微調TravelAdapter讓無結果時顯示Alert
>     
> **GUI處理類別更動**
> - (新增)**彩蛋**
> - (修改)**搜尋frag**:篩選地區移植到搜尋欄、日期鈕長按取消
> - (修改)**訂購acti**:調整一些限制、例外
> - (新增)**動作完成anim**:成功訂購、移除以及~~修改~~
> 
> **GUI資料更動**
> - 加入彩蛋drawable
> - 加入seekbar_thumb

--- 
> 開發方向
> - 
> - 萃取觀察者模式
> - Invoker實例是否提升層級
> - 整理程式碼 重構
> - Concrete Command
>     - GetTravelsResultCommand 擴充
>     - 還有更多...
> - 咖波Alert處理Exception
> - 修改訂單實作 以及畫面處理(seekbar)
> - 搜尋清單的下一頁功能
> - 日期按下即時更新
> - 排序換右上角
> - 畫面可以用好看一些 (都用完之後可以改)
> - Auth 讀寫資料庫設定
### alpha-1.5
> 版本資訊：
> -
> 
> **概述**
> > 新增即時抓資料圖片的方法
> > 修改選地區的命令至堪用
> > 導入AsyncTask
> 
> **類別更動**
> - 新增**ImageFromURLTask**: 用於以網址設定圖片
> - 微調瀚文的功能
> - 改造TravelAdapter: 提供static方法設置圖片
> - 調整GetTravelsResultCommand 的選擇地區
> - 建立訂單重新啟用(仍不會反應到已購人數)
> - 完成搜尋功能部份的acti生命週期控制
> - 改北韓國家名欄位為"bomb"以避免無搜尋結果
> - 新增具體命令
>     - 查找地區命令 LookForCodesCommand
>     
> **GUI處理類別更動**
> - (新增)**訂購acti**:訂購頁面(bar式選數量)
> - (新增)**讀取dia**: 用於讀取條顯示
> - 解決標題字太多的問題
> 
> **GUI資料更動**
> - 加入一張PurchaseForm layout
> - 加入seek bar layout
