# taro-oriented-project
## log
###### tags: `程式專案`,`紫芋導向`,`start travel`
### beta-1.1
> 版本資訊：
> -
> 
> **概述**
> > HomeFrag修改
> > UserFrag修改
> > 會員功能完整化
> 
> **類別更動**
> - (新增)[FireImage Helper package](https://https://hackmd.io/@3kyPThCWTuy3rfeeIvxfQQ/HybKMB1qU)
> - (修改)**NameDBObserver**:根據購買量更新會員狀態
> 
> **GUI處理類別更動**
> - (修改)**HomeFrag**:ImageSlider、Scroller
> - (微調)**SearchFrag**:排序改icon、運作模式改為toggle
> - (修改)**UserFrag**:會員頭像、會員部分改窄、會員狀態、登出
> 
> **GUI資料更動**
> - 新增單筆slide

--- 
> 開發方向
> - 
> - 萃取觀察者模式
> - Invoker實例是否提升層級
> - 整理程式碼 註解
> - 搜尋清單數量問題
> - Purchase : 改layout
> - userFrag : 單項改layout、改資訊量
> - userOrder : 加狀態
> - login,signup ： 改layout?

### beta-1.0
> 版本資訊：
> -
> 
> **概述**
> > Auth 讀寫資料庫設定
> > 清單GUI更新
> 
> **類別更動**
> - (新增)**OnOneOffClickListener**:解決listner聽兩次的狀況
> - (微調)**OrdersDBObserver**:吃進來的list移除「已經開始且沒到下限」的order
> - user/orders中的travel跟travels的同步
> 
> **GUI處理類別更動**
> - (微調)**userFrag**:登出功能
> 
> **GUI資料更動**
> - SearchFrag : 改篩選圖示、單項travel變矮、差幾人成團以及已經成團只剩多少名額、不能訂(人滿、過期)灰掉
> - TravelDetail ： 改layout