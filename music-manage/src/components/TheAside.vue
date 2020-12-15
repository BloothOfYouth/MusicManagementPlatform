<template>
    <div class="sidebar">
        <el-menu
                class="sidebar-el-menu"
                :default-active="activePath"
                :collapse="collapse"
                background-color="#334256"
                text-color="#ffffff"
                active-text-color="#20a0ff"
                router
        >
            <ul>
                <template>
                    <el-menu-item  v-for="item in items" :index="item.path" :key="item.path" @click="saveActivePath(item.path)">
                        <i :class="item.icon"></i>
                        <span slot="title">{{item.title}}</span>
                    </el-menu-item>
                </template>
            </ul>

        </el-menu>
    </div>
</template>

<script>
    import bus from "../assets/js/bus";
    export default {
        name: "TheAside",
        data() {
            return {
                collapse: false,//默认不折叠
                items: [
                    {
                        icon: 'el-icon-s-data',
                        path: '/Info',
                        title: '系统首页'
                    },
                    {
                        icon: 'el-icon-s-custom',
                        path: '/User',
                        title: '用户管理'
                    },
                    {
                        icon: 'el-icon-user-solid',
                        path: '/Singer',
                        title: '歌手管理'
                    },
                    {
                        icon: 'el-icon-s-order',
                        path: '/SongList',
                        title: '歌单管理'
                    },
                ],
                activePath: '/Info',
            }
        },
        methods: {
            saveActivePath(activePath) {
                localStorage.setItem("activePath",activePath);
                this.activePath = activePath;
            }
        },
        created() {
            // 通过Bus进行组件间的通信，来折叠侧边栏
            // 接收TheHeader组件发送来的信息
            bus.$on('collapseChange',msg => {
                this.collapse = msg
            })
            if (localStorage.getItem("activePath")) {
                this.activePath = localStorage.getItem("activePath"); // 取出localStorage里的path 动态修改activePath
            }
            if (this.$route.path === "/Info") {
                this.activePath = "/Info"
            }
        }
    }
</script>

<style scoped lang="less">
    .sidebar {
        position: absolute;
        left: 0;
        top: 70px;
        bottom: 0;
        background-color: #334256;
        ul {
            height: 100%;
        }
    }

    .sidebar-el-menu:not(.el-menu--collapse) {//只有el-menu上的collapse是false，这个css样式才起作用
        width: 120px;
    }
</style>
