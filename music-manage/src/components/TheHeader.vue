<template>
    <div class="header">
        <!-- 折叠图片 -->
        <div class="collapse-btn" @click="collapseChange">
            <i class="el-icon-menu"></i>
        </div>
        <div class="logo">music后台管理</div>
        <div class="header-right">
            <div class="btn-fullscreen" @click="handleFullScreen">
                <el-tooltip effect="dark" :content="isFullScreen?'取消全屏':'全屏'" placement="bottom">
                    <i class="el-icon-rank"></i>
                </el-tooltip>
            </div>
            <div class="user-avator">
                <img src="../assets/img/user.jpg" alt="">
            </div>
            <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                <span class="el-dropdown-link">
                    {{username}}
                    <i class="el-icon-arrow-down el-icon--right"></i>
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
    </div>
</template>

<script>
    import bus from "../assets/js/bus";
    export default {
        name: "TheHeader",
        data() {
            return{
                collapse: false,
                isFullScreen: false
            }
        },
        methods: {
            // 侧边栏折叠
            collapseChange() {
                this.collapse = !this.collapse
                // 发送信息给TheAside组件
                bus.$emit('collapseChange',this.collapse)
            },
            // 全屏事件
            handleFullScreen() {
                if (this.isFullScreen) {
                    // 下面全屏功能对每个浏览器做兼容
                    if (document.exitFullscreen) {
                        document.exitFullscreen()
                    }else if(document.webkitCancelFullScreen){ // safari  chrome
                        document.webkitCancelFullScreen()
                    }else if(document.mozCancelFullScreen){ // firefox
                        document.mozCancelFullScreen()
                    }else if(document.msExitFullScreen){ // IE
                        document.msExitFullScreen()
                    }
                }else {
                    let element = document.documentElement;
                    if (element.requestFullscreen) {
                        element.requestFullscreen()
                    }else if(element.webkitRequestFullScreen) { // safari  chrome
                        element.webkitRequestFullScreen()
                    }else if(element.mozRequestFullScreen) { // firefox
                        element.mozRequestFullScreen()
                    }else if(element.msRequestFullScreen) { // IE
                        element.msRequestFullScreen()
                    }
                }
                this.isFullScreen = !this.isFullScreen
            },
            handleCommand(command) {
                if (command == "logout") {
                    localStorage.removeItem("JWT_TOKEN")
                    localStorage.removeItem("username");
                    this.$router.push("/")
                }
            }
        },
        computed: {
            username() {
                return window.localStorage.getItem("username")
            }
        }
    }
</script>

<style scoped lang="less">
    .header {
        position: relative;//直接相对定位，就会再最上方
        background-color: #253041;
        width: 100%;
        height: 70px;
        font-size: 22px;
        color: #ffffff;
    }
    .collapse-btn {
        float: left;
        padding: 0 21px;
        cursor: pointer;
        line-height: 70px;
    }
    .logo {
        position: relative;
        float: left;
        line-height: 70px;
    }
    .header-right {
        float: right;
        display: flex;
        padding-right: 16px;
        height: 70px;
        text-align: center;
        line-height: 70px;
    }
    .btn-fullscreen {
        cursor: pointer;
        margin-right: 10px;
        transform: rotate(45deg);
        font-size: 24px;
    }
    .user-avator {
        img {
            vertical-align: middle;
            margin-top: -5px;
            width: 40px;
            height: 40px;
            border-radius: 40px;
        }
    }
    .user-name {
        margin-left: 10px;
    }
    .el-dropdown-link {
        color: #ffffff;
        cursor: pointer;
    }
    .el-dropdown-menu {
        top: 50px !important;
        padding: 2px 0;
    }
</style>
